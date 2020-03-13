package com.yy.miaosha.controller;

import com.yy.miaosha.domain.Goods;
import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.domain.OrderInfo;
import com.yy.miaosha.result.CodeMsg;
import com.yy.miaosha.result.Result;
import com.yy.miaosha.service.GoodsService;
import com.yy.miaosha.service.OrderService;
import com.yy.miaosha.vo.GoodsVO;
import com.yy.miaosha.vo.OrderDetailVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-25 15:40
 **/
@Controller
@RequestMapping(value = "/order")
public class OrderController {
    @Resource
    OrderService orderService;
    @Resource
    GoodsService goodsService;

    @RequestMapping(value = "/detail")
    @ResponseBody
    public Result getOrder(Model model, MiaoshaUser user,
                           @RequestParam("orderId") long orderId) {
        //这个判空功能很多类都有，可以通过拦截器来统一实现，避免重复造轮子
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        OrderInfo orderInfo = orderService.getOrderById(orderId);
        if (orderInfo == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = orderInfo.getGoodsId();
        GoodsVO goods = goodsService.getGoodsByGoodsId(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(orderInfo);
        vo.setGoods(goods);
        return Result.success(vo);

    }

}