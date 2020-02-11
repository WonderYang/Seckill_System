package com.yy.miaosha.controller;

import com.yy.miaosha.domain.MiaoshaOrder;
import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.domain.OrderInfo;
import com.yy.miaosha.result.CodeMsg;
import com.yy.miaosha.service.GoodsService;
import com.yy.miaosha.service.MiaoshaService;
import com.yy.miaosha.service.OrderService;
import com.yy.miaosha.vo.GoodsVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-10 14:27
 **/

@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {
    @Resource
    GoodsService goodsService;
    @Resource
    OrderService orderService;
    @Resource
    MiaoshaService miaoshaService;

    @RequestMapping("/do_miaosha")
    public String miaosha(Model model, MiaoshaUser miaoshaUser,
                          @RequestParam("goodsId") long goodsId) {
        if (miaoshaUser == null) {
            return "login";
        }
        //判断库存
        GoodsVO goodsVO = goodsService.getGoodsByGoodsId(goodsId);
        int stock = goodsVO.getStockCount();
        if (stock <= 0) {
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_OVER.getMsg());
            return "miaosha_fail";
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(miaoshaUser.getId(), goodsId);
        if (order != null) {
            model.addAttribute("errmsg", CodeMsg.MIAOSHA_REPEAT.getMsg());
            return "miaosha_fail";
        }

        //秒杀
        OrderInfo orderInfo = miaoshaService.miaosha(miaoshaUser, goodsVO);
        model.addAttribute("orderInfo", orderInfo);
        model.addAttribute("goods", goodsVO);

        return "order_detail";

    }
}