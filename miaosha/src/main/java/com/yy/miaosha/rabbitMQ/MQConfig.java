package com.yy.miaosha.rabbitMQ;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-26 18:19
 **/
@Configuration
public class MQConfig {
    //项目中使用Direct模式
    public static final String MIAOSHA_QUEUE = "miaosha_queue";



    //这些都是测试队列的几种模式的时候用的，与项目无关
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic_queue1";
    public static final String TOPIC_QUEUE2 = "topic_queue2";
    public static final String HEADERS_QUEUE = "headers_queue";
    public static final String TOPIC_EXCHANGE = "topic_exchange";
    public static final String FANOUT_EXCHANGE = "fanout_exchange";
    public static final String HEADERS_EXCHANGE = "headers_exchange";



    /**
     * Direct模式交换机
     * @return
     */
    @Bean
    public Queue queue() {
        //第二个参数代表是否是异步队列
        return new Queue(QUEUE, true);
    }

    /**
     * 项目中用到的queue
     * @return
     */
    @Bean
    public Queue miaoshaQueue() {
        return new Queue(MIAOSHA_QUEUE, true);
    }

    /**
     * Topic模式交换机
     * @return
     */
    @Bean
    public Queue topicQueue1() {
        //第二个参数代表是否是异步队列
        return new Queue(TOPIC_QUEUE1, true);
    }

    @Bean
    public Queue topicQueue2() {
        //第二个参数代表是否是异步队列
        return new Queue(TOPIC_QUEUE2, true);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }

    /**
     * fanout模式交换器Exchange
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }


    /**
     * 将消息队列和topic交换机进行绑定
     * @return
     */
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with("topic.key1");
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with("topic.#");
    }


    /**
     * 将消息队列和fanout交换机进行绑定
     * @return
     */
    @Bean
    public Binding fanoutBinding1() {
        //fanout模式下不用设置key，因为是广播嘛，设置了有什么意义呢
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding2() {
        //fanout模式下不用设置key，因为是广播嘛，设置了有什么意义呢
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }


    /**
     * headers模式交换机Exchange
     * @return
     */
    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    @Bean
    public Queue headerQueue() {
        return new Queue(HEADERS_QUEUE);
    }
    @Bean
    public Binding headerBinding() {
        Map<String, Object> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");
        return BindingBuilder.bind(headerQueue()).to(headersExchange()).whereAll(map).match();
    }



}