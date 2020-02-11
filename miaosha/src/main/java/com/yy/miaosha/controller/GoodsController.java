package com.yy.miaosha.controller;

import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.service.GoodsService;
import com.yy.miaosha.service.MiaoshaUserService;
import com.yy.miaosha.vo.GoodsVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-08 10:40
 **/
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    MiaoshaUserService miaoshaUserService;
    @Resource
    GoodsService goodsService;

    @RequestMapping("/to_list")
    public String getGoodsList(Model model, MiaoshaUser miaoshaUser) {
        model.addAttribute("user",miaoshaUser);

        List<GoodsVO> goodsVOList = goodsService.listGoodVO();
        model.addAttribute("goodsList", goodsVOList);
        return "goods_list";
    }

    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser miaoshaUser,
                         @PathVariable("goodsId") long goodsId) {
        model.addAttribute("user", miaoshaUser);
        GoodsVO goodsVO = goodsService.getGoodsByGoodsId(goodsId);
        model.addAttribute("goods", goodsVO);
        long start = goodsVO.getStartDate().getTime();
        long end = goodsVO.getEndDate().getTime();
        long now = System.currentTimeMillis();

        //秒杀状态：0为开始 1进行中 2已结束
        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if (now < start) {
            miaoshaStatus = 0;
            remainSeconds = (int)(start - now)/1000;
        }else if (now > end) {
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else {
            miaoshaStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);

        System.out.println(goodsVO.getStartDate() );
        return "goods_detail";
    }
}