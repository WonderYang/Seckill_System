package com.yy.miaosha.redis;

import com.alibaba.fastjson.JSON;
import com.yy.miaosha.redis.prefix.KeyPrefix;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-03 11:30
 **/

@Service
public class RedisService {

    @Resource
    JedisPool jedisPool;

    /**
     * 获取单个对象
     * @param keyPrefix
     * @param key
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix keyPrefix, String key, Class<T> tClass) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            String str = jedis.get(realKey);
            T t = stringToBean(str, tClass);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }


    /**
     * 设置对象
     * @param keyPrefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix keyPrefix,String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String val = beanToString(value);
            if(val == null || val.length()==0) {
                return false;
            }
            String realKey = keyPrefix.getPrefix()+key;
            int seconds = keyPrefix.expireSeconds();
            //如果过期时间小于等于零，直接设置
            if(seconds <= 0) {
                jedis.set(realKey, val);
            }else {
                jedis.setex(realKey, seconds, val);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断某个key是否存在
     * @param keyPrefix
     * @param key
     * @return
     */
    public boolean exists(KeyPrefix keyPrefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除某个key
     * @param keyPrefix
     * @param key
     * @return
     */
    public boolean delete(KeyPrefix keyPrefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            long res = jedis.del(realKey);
            return res>0;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 将某个key的值自减一，并返回减之后的值，如果当前key的值类型不是数字类型（Long），则会
     * @param keyPrefix
     * @param key
     * @return
     */
    public Long decr(KeyPrefix keyPrefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 将某个key的值自加一
     * @param keyPrefix
     * @param key
     * @return
     */
    public Long incr(KeyPrefix keyPrefix,String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix()+key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 用fastJson将字符串转化为对象
     * @param value
     * @param tClass
     * @param <T>
     * @return
     */
    public static  <T> T stringToBean(String value, Class<T> tClass) {
        if(value == null || value.length()==0) {
            return null;
        }
        if(tClass==int.class || tClass==Integer.class) {
            return (T) Integer.valueOf(value);

        }else if(tClass == String.class) {
            return (T) value;
        }else if (tClass == Long.class) {
            return (T)Long.valueOf(value);
        }else {
            return JSON.toJavaObject(JSON.parseObject(value), tClass);
        }
    }

    /**
     * 用fastJson将对象转化为字符串
     * @param value
     * @param <T>
     * @return
     */
    public static  <T> String beanToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> cla = value.getClass();
        if(cla==int.class || cla==Integer.class) {
            return "" + value;

        }else if(cla == String.class) {
            return (String)value;
        }else if (cla == Long.class) {
            return "" + value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 用完后将对象返回到连接池
     * @param jedis
     */
    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }


}