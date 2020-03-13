package com.yy.miaosha.redis.prefix;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-14 15:33
 **/
public class GoodsKey extends BasePrefix {

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    //页面缓存的有效期时间一般比较短，这里设置为60s
    public static GoodsKey getGoodsList = new GoodsKey(60,"gl");

    public static GoodsKey getMiaoshaGoodsStock = new GoodsKey(0,"gs");


}