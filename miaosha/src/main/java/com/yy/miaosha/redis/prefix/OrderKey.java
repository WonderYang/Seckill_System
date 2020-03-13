package com.yy.miaosha.redis.prefix;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-25 20:36
 **/
public class OrderKey extends BasePrefix {

    public OrderKey(int expireSeconds,String prefix) {
        super(expireSeconds, prefix);
    }

    //成功下单后，订单会在Redis中永久保存
    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey(0, "moug");
}