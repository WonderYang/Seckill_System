package com.yy.miaosha.rabbitMQ;

import com.yy.miaosha.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-26 18:13
 **/

@Service
public class MQSender {
    @Resource
    RedisService redisService;

    @Autowired
    AmqpTemplate amqpTemplate;
    public static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    /**
     * 发送秒杀信息，用的是Direct模式交换机
     */
    public void sendMiaoshaMessage(MiaoshaMessage miaoshaMessage) {
        String msg = RedisService.beanToString(miaoshaMessage);
        log.info("sendMessage:"+msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }


    //下面的方法都是测试那几种模式的时候用的，项目中没用
    public void send(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("sendMessage: "+ msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);

    }

    public void sendTopic(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send topic message:"+msg);
        //这能命中两个消息队列
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
        //这只能命中一个，命中topic.#那个
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
    }

    public void sendFanout(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send topic message:"+msg);
        //这能命中两个消息队列,注意这里虽然不需要key，但是一定的输入一个空参数，即s1必须有
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "",msg);
    }

    public void sendHeader(Object message) {
        String msg = RedisService.beanToString(message);
        log.info("send fanout message:" + msg);
        MessageProperties properties = new MessageProperties();
        properties.setHeader("key1", "value1");
        properties.setHeader("key2", "value2");
        Message obj = new Message(msg.getBytes(), properties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
    }


}