package com.yy.miaosha.controller;

import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.redis.RedisService;
import com.yy.miaosha.result.Result;
import com.yy.miaosha.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: miaosha
 * @description: 压力测试时，用这个类来测试获取用户的QPS
 * @author: yangyun86
 * @create: 2020-02-09 10:49
 **/
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
    MiaoshaUserService userService;
	
	@Autowired
    RedisService redisService;
	
    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoshaUser> info(Model model, MiaoshaUser user) {
        return Result.success(user);
    }
    
}
