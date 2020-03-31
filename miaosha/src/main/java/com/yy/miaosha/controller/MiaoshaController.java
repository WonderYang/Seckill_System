package com.yy.miaosha.controller;

import com.yy.miaosha.domain.MiaoshaOrder;
import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.domain.OrderInfo;
import com.yy.miaosha.rabbitMQ.MQSender;
import com.yy.miaosha.rabbitMQ.MiaoshaMessage;
import com.yy.miaosha.redis.RedisService;
import com.yy.miaosha.redis.prefix.GoodsKey;
import com.yy.miaosha.result.CodeMsg;
import com.yy.miaosha.result.Result;
import com.yy.miaosha.service.GoodsService;
import com.yy.miaosha.service.MiaoshaService;
import com.yy.miaosha.service.OrderService;
import com.yy.miaosha.utils.UUIDUtil;
import com.yy.miaosha.vo.GoodsVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-10 14:27
 **/

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {
    @Resource
    GoodsService goodsService;
    @Resource
    OrderService orderService;
    @Resource
    MiaoshaService miaoshaService;
    @Resource
    RedisService redisService;
    @Resource
    MQSender mqSender;

    private HashMap<Long, Boolean> isOverFlagMap = new HashMap<>();

    //系统初始化加载商品库存
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> goodsList = goodsService.listGoodVO();
        if(goodsList == null) {
            return;
        }
        for(GoodsVO goods : goodsList) {
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), goods.getStockCount());
            //内存标记
            isOverFlagMap.put(goods.getId(), false);
        }
    }

    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoshaUser miaoshaUser,
                          @RequestParam("goodsId") long goodsId,
                          @PathVariable("path") String path) {
        if (miaoshaUser == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //验证path
        boolean verify =  miaoshaService.checkPath(miaoshaUser, goodsId, path);
        if (!verify) {
            return Result.error(CodeMsg.REQUEST_ILLEGLE);
        }

        //如果成立则代表该商品预减库存已经小于零了，没必要再去访问Redis来预减库存了；
        if (isOverFlagMap.get(goodsId)) {
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        //返回的是减一之后的值
        long stockCount = redisService.decr(GoodsKey.getMiaoshaGoodsStock, goodsId+"");
        if (stockCount < 0) {
            isOverFlagMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAOSHA_OVER);
        }

        System.out.println(miaoshaUser.getId()+"跳过了Redis");

        //判断是否已经秒杀到了,即不能重复秒杀(这个是从Redis中查的，我们下单成功会将订单存到Redis)
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(miaoshaUser.getId(), goodsId);
        if (order != null) {
            System.out.println(miaoshaUser.getId()+"不能重复秒杀");
            return Result.error(CodeMsg.MIAOSHA_REPEAT);
        }
        //请求入队
        System.out.println(miaoshaUser.getId()+"成功进入消息队列");
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setMiaoshaUser(miaoshaUser);
        mm.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(mm);

        return Result.success(0); //0代表排队中，前端会判断这个值


//        //判断库存
//        GoodsVO goodsVO = goodsService.getGoodsByGoodsId(goodsId);
//        int stock = goodsVO.getStockCount();
//        if (stock <= 0) {
//            return Result.error(CodeMsg.MIAOSHA_OVER);
//        }
//        //判断是否已经秒杀到了,即不能重复秒杀
//        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(miaoshaUser.getId(), goodsId);
//        if (order != null) {
//            return Result.error(CodeMsg.MIAOSHA_REPEAT);
//        }
//
//        //秒杀
//        OrderInfo orderInfo = miaoshaService.miaosha(miaoshaUser, goodsVO);
//
//        System.out.println(miaoshaUser.getId()+"已经秒杀！！！");
//
//        return Result.success(orderInfo);

    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     * */
    @RequestMapping(value="/result", method=RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model,MiaoshaUser user,
                                      @RequestParam("goodsId")long goodsId) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }


    @RequestMapping(value="/getMiaoshaPath", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(Model model,MiaoshaUser user,
                                      @RequestParam("goodsId")long goodsId,
                                         @RequestParam(value="verifyCode", defaultValue="0")int verifyCode) {
        model.addAttribute("user", user);
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //验证验证码是否正确，验证码答案在生成的时候就已经放在Redis中了
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGLE);
        }

        String uuid =  miaoshaService.createPath(user, goodsId);
        return Result.success(uuid);
    }

    /**
     * 生成数学公式验证码图片
     * @param response
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value="/verifyCode", method=RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaVerifyCod(HttpServletResponse response, MiaoshaUser user,
                                              @RequestParam("goodsId")long goodsId) {
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        try {
            BufferedImage image  = miaoshaService.createVerifyCode(user, goodsId);
            OutputStream out = response.getOutputStream();
            //把图片直接输出到我们的输出流，即直接输出到前端页面
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
            return null;
        }catch(Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.MIAOSHA_FAIL);
        }
    }
}