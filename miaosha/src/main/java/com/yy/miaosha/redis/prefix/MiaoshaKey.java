package com.yy.miaosha.redis.prefix;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-03-08 17:42
 **/
public class MiaoshaKey extends BasePrefix{
    private MiaoshaKey( int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static MiaoshaKey isGoodsOver = new MiaoshaKey(0, "go");
    public static MiaoshaKey getMiaoshaPath = new MiaoshaKey(60, "mp");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300, "vc");

}