package com.yy.miaosha.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: miaosha
 * @description:这里我当时遇到了一个大坑，如果是extends WebMvcConfigurationSupport这个类来实现，静态资源就会失效，需要在里面
 * 重写一些方法；
 * @author: yangyun86
 * @create: 2020-02-08 18:34
 **/

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    UserArgumentResolver userArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(userArgumentResolver);
    }




}