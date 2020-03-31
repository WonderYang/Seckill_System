package com.yy.miaosha.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @program: miaosha
 * @description:  测试Redis的连接
 * @author: yangyun86
 * @create: 2020-02-09 10:49
 **/
public class Test {
    @Resource
    ThymeleafViewResolver thymeleafViewResolver;
    public static void main(String[] args) {
      Jedis jedis = new Jedis("192.168.2.101", 6379);
      System.out.println(jedis.ping());
//
//        File file = new File("/Users/yangyun/Documents/miaoshaData/tokens.txt");
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(file);
       // HashMap<String, String> hashMap = new HashMap<>();



    }


}