package com.yy.miaosha.redis.prefix;

/**
 * @program: miaosha
 * @description: 这个类用来封装User的前缀
 * @author: yangyun86
 * @create: 2020-02-03 17:27
 **/
public class UserKey extends BasePrefix {


    private UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");

}