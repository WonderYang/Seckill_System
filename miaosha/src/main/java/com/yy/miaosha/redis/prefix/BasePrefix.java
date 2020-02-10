package com.yy.miaosha.redis.prefix;

import com.yy.miaosha.redis.prefix.KeyPrefix;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-03 17:09
 **/
public class BasePrefix implements KeyPrefix {
    private int expireSeconds;
    private String prefix;

    //过期时间expireSeconds默认为0，代表用不过期
    public BasePrefix(String prefix) {
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":"+prefix;
    }
}