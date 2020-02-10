package com.yy.miaosha;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;

@SpringBootApplication
public class MainApplication {

    //阿里的json转换器，我没有这个的话返回给前端对象时会报错：无转换器
    @Bean
    public HttpMessageConverters fastHttpMessageConverters() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        converter.setFastJsonConfig(fastJsonConfig);
        HttpMessageConverter<?> converter2 = converter;
        return new HttpMessageConverters(converter2);
    }
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainApplication.class, args);
    }
}
