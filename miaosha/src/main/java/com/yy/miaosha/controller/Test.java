package com.yy.miaosha.controller;

import redis.clients.jedis.Jedis;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-09 10:49
 **/
public class Test {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.2.103", 6379);
        System.out.println(jedis.ping());
    }


}