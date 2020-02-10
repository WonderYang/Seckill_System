package com.yy.miaosha.controller;

import com.yy.miaosha.redis.RedisService;
import com.yy.miaosha.result.Result;
import com.yy.miaosha.service.MiaoshaUserService;
import com.yy.miaosha.utils.UUIDUtil;
import com.yy.miaosha.vo.LoginVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @program: miaosha
 * @description: 登陆界面
 * @author: yangyun86
 * @create: 2020-02-04 15:22
 **/
@Controller
@RequestMapping("/login")
public class LoginController {
    @Resource
    MiaoshaUserService userservice;
    @Resource
    RedisService redisService;

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping("/to_login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVO loginVO) {
        log.info(loginVO.toString());
        userservice.login(response, loginVO);
        //如果登陆不成功，login方法就会抛出异常，然后异常处理器就会捕捉到异常，然后进行处理；

        return Result.success(true);

    }
}