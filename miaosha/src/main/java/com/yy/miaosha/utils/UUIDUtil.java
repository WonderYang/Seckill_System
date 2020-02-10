package com.yy.miaosha.utils;

import java.util.UUID;

/**
 * @program: miaosha
 * @description: 生成随机的UUID
 * @author: yangyun86
 * @create: 2020-02-07 20:43
 **/
public class UUIDUtil {
    public static String uuid() {
        //生成出来的UUID带有"-"，一般我们要丢掉这些，剩下的就都是字符串,很长的字符串；
        return UUID.randomUUID().toString().replace("-", "");
    }

}