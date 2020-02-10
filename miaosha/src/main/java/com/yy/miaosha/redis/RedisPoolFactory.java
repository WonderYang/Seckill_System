package com.yy.miaosha.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * @program: miaosha
 * @description: 配置Redis的连接池JedisPool，并且将其注入Spring中
 * @author: yangyun86
 * @create: 2020-02-03 13:00
 **/

@Service
public class RedisPoolFactory {

    @Resource
    RedisConfig redisConfig;

    //@Bean用于注解方法时，会被spring自动作为一个bean进行注入，
    //bean的类型为该方法的返回类型，
    //bean的id为方法名称
    //方法参数，会通过spring自动注入
    @Bean
    public JedisPool jedisPoolFactory() {
        //设置Redis连接池配置对象
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        jedisPoolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        //我们是以秒来配置的，所以这里需要乘以1000，这里参数接受的是毫秒
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait() * 1000);

        //配置Redis连接池，Redis有16个库，最后一个参数0代表使用第一个库，不配也默认从0开始
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort(),
                redisConfig.getTimeout()*1000, redisConfig.getPassword(), 0);
        return jedisPool;
    }
}