package com.yy.miaosha.service;

import com.yy.miaosha.domain.MiaoshaOrder;
import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.domain.OrderInfo;
import com.yy.miaosha.vo.GoodsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-10 15:15
 **/
@Service
public class MiaoshaService {
    @Resource
    GoodsService goodsService;
    @Resource
    OrderService orderService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVO goods) {

        //1. 减库存
        goodsService.reduceStock(goods);
        OrderInfo orderInfo = orderService.createOrder(user, goods);
        //2. 生成新订单并返回
        return orderInfo;
    }
}