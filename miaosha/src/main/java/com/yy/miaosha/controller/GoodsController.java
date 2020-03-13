package com.yy.miaosha.controller;

import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.redis.RedisService;
import com.yy.miaosha.result.Result;
import com.yy.miaosha.service.GoodsService;
import com.yy.miaosha.service.MiaoshaUserService;
import com.yy.miaosha.vo.GoodsDetailVO;
import com.yy.miaosha.vo.GoodsVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Resource
    RedisService redisService;
    ////只要使用了Thymeleaf，这个类就会在Spring中自动生成；
    @Resource
    ThymeleafViewResolver thymeleafViewResolver;


    @RequestMapping(value="/to_list")
    //@ResponseBody
    public String getGoodsList(HttpServletRequest request, HttpServletResponse response,
                               Model model, MiaoshaUser miaoshaUser) {
        if (miaoshaUser != null) {
            model.addAttribute("user",miaoshaUser);
        }else {
            System.out.println("cookie已失效，没拿到秒杀user");
            return "login";
        }
        List<GoodsVO> goodsVOList = goodsService.listGoodVO();
        model.addAttribute("goodsList", goodsVOList);
        return "goods_list";

        //取缓存——————这里用到页面缓存技术
//        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
//        if(!StringUtils.isEmpty(html)) {
//            System.out.println(GoodsKey.getGoodsList.getPrefix()+"");
//            System.out.println(html);
//            return html;
//        }
//        //手动渲染
//        WebContext ctx = new WebContext(request,response,
//                request.getServletContext(),request.getLocale(), model.asMap());
//        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", ctx);
//        System.out.println(html);
//        if (!StringUtils.isEmpty(html)) {
//            redisService.set(GoodsKey.getGoodsList, "", html);
//        }
//        return html;

    }

    //这个控制器用了前后端分离
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVO> detail1(Model model, MiaoshaUser miaoshaUser,
                                         @PathVariable("goodsId") long goodsId) {
        GoodsVO goodsVO = goodsService.getGoodsByGoodsId(goodsId);
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

        GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
        goodsDetailVO.setMiaoshaStatus(miaoshaStatus);
        goodsDetailVO.setGoods(goodsVO);
        goodsDetailVO.setRemainSeconds(remainSeconds);
        goodsDetailVO.setUser(miaoshaUser);
        return Result.success(goodsDetailVO);
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

        //System.out.println(goodsVO.getStartDate() );
        return "goods_detail";
    }
}