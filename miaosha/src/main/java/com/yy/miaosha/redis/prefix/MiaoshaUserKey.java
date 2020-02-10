package com.yy.miaosha.redis.prefix;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-07 21:18
 **/
public class MiaoshaUserKey extends BasePrefix {
    public static final int TOKEN_EXPIRE = 3600*24*2;

    public MiaoshaUserKey(int expireSeconds ,String prefix) {
        super(expireSeconds ,prefix);
    }
    //设置默认过期时间为2天
    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE,"tk");

}