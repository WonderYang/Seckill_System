package com.yy.miaosha.redis.prefix;

public interface KeyPrefix {

    //过期时间
    int expireSeconds();

    //获取前缀
    String getPrefix();
}
