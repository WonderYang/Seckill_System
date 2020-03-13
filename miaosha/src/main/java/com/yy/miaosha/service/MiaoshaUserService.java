package com.yy.miaosha.service;

import com.yy.miaosha.dao.MiaoshaUserDao;
import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.exception.GlobalException;
import com.yy.miaosha.redis.RedisService;
import com.yy.miaosha.redis.prefix.MiaoshaUserKey;
import com.yy.miaosha.result.CodeMsg;
import com.yy.miaosha.utils.MD5Util;
import com.yy.miaosha.utils.UUIDUtil;
import com.yy.miaosha.vo.LoginVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-06 11:54
 **/
@Service
public class MiaoshaUserService {
    @Resource
    MiaoshaUserDao miaoshaUserDao;
    @Resource
    RedisService redisService;
    //传给前端的Cookie名
    public static final String COOKIE_NAME_TOKEN = "token";

    public MiaoshaUser getById(long id) {
        //从缓存中获取
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.getByID,""+id, MiaoshaUser.class);
        if (miaoshaUser != null) {
            return miaoshaUser;
        }
        //缓存中不存在，查询数据库并入缓存；
        miaoshaUser = miaoshaUserDao.getById(id);
        if (miaoshaUser != null) {
            redisService.set(MiaoshaUserKey.getByID,""+id, miaoshaUser);
        }
        return miaoshaUser;
    }

    /**
     * 用户登陆的校验，首先校验手机号（id）存在，再校验其密码是否正确；
     * @param loginVO
     * @return
     */
    public String login(HttpServletResponse response, LoginVO loginVO) {
        if (loginVO == null) {
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVO.getMobile();
        String formPass = loginVO.getPassword();
        MiaoshaUser miaoshaUser = getById(Long.parseLong(mobile));
        //验证手机号是否存在，不存在则代表登陆用户都不存在，直接返回错误；
        if (miaoshaUser == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //手机号（用户）存在，此时来验证密码是否正确；
        String dbSalt = miaoshaUser.getSalt();
        //由用户密码和数据库所存salt计算出来的值
        String calcPass = MD5Util.formPassToDBPass(formPass, dbSalt);
        String dbPass = miaoshaUser.getPassword();

        if(!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }

        //生成Cookie
        String token = UUIDUtil.uuid();
        addCookie(response, miaoshaUser, token);
        return token;

    }

    /**
     * 生成Cookie
     * @param response
     * @param miaoshaUser
     */
    private void addCookie(HttpServletResponse response, MiaoshaUser miaoshaUser, String token) {
        redisService.set(MiaoshaUserKey.token, token, miaoshaUser);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    /**
     * 从Redis缓存中根据token获取秒杀用户对象；
     * @param token
     * @return
     */
    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if(StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser miaoshaUser = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if(miaoshaUser != null) {
            addCookie(response, miaoshaUser, token);
        }
        return miaoshaUser;
    }
}