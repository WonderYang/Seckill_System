package com.yy.miaosha.controller;

import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.service.GoodsService;
import com.yy.miaosha.service.MiaoshaUserService;
import com.yy.miaosha.vo.GoodsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
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

    @RequestMapping("/to_list")
    public String getGoodsList(Model model, MiaoshaUser miaoshaUser) {
        model.addAttribute("user",miaoshaUser);

        List<GoodsVO> goodsVOList = goodsService.listGoodVO();
        model.addAttribute("goodsList", goodsVOList);
        return "goods_list";
    }
}