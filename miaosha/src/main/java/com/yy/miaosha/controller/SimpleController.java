package com.yy.miaosha.controller;

import com.yy.miaosha.domain.User;
import com.yy.miaosha.rabbitMQ.MQSender;
import com.yy.miaosha.redis.RedisService;
import com.yy.miaosha.redis.prefix.KeyPrefix;
import com.yy.miaosha.redis.prefix.UserKey;
import com.yy.miaosha.result.CodeMsg;
import com.yy.miaosha.result.Result;
import com.yy.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping("/demo")
public class SimpleController {

    @Resource
    UserService userService;

    @Resource
    RedisService redisService;

    @Autowired
    MQSender mqSender;

    @RequestMapping("/demo1")
    public String thymeleaf(Model model) {
        model.addAttribute("name","yangyun");
        return "hello";
    }

    @RequestMapping("/demo2")
    @ResponseBody
    public Result<String> test1() {
        //return Result.success("hello");
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @RequestMapping("/demo3")
    @ResponseBody
    public Result<User> testDB() {
        User user = userService.getUserById(1);
        return Result.success(user);
    }

    //测试数据库事物
    @RequestMapping("/demo4")
    @ResponseBody
    public Result<Boolean> testTx() {
        return Result.success(userService.tx());
    }

    @RequestMapping("/demo5")
    @ResponseBody
    public Result<User> testRedis() {

        User user = new User(22222,"Bob");
        //最终拼接的key为：UserKey:id1
        redisService.set(UserKey.getById,"1",user);
        User user1 = redisService.get(UserKey.getById,"1",User.class);
        return Result.success(user1);

    }

    @RequestMapping("/demo6")
    @ResponseBody
    public Result<Boolean> testRedis2() {
        boolean res =  redisService.exists(UserKey.getById, "1");
        return Result.success(res);
    }

    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> test6() {
        mqSender.send("hello rabbitmq!!!!");
        return Result.success("hello");
    }

    @RequestMapping("/mqTopic")
    @ResponseBody
    public Result<String> test7() {
        mqSender.sendFanout("hello rabbitmq!!!!");
        return Result.success("hello");
    }

    @RequestMapping("/header")
    @ResponseBody
    public Result<String> test8() {
        mqSender.sendHeader("hello rabbitmq!!!!");
        return Result.success("hello");
    }
}
