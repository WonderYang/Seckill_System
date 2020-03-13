package com.yy.miaosha.rabbitMQ;

import com.yy.miaosha.domain.MiaoshaOrder;
import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.domain.OrderInfo;
import com.yy.miaosha.redis.RedisService;
import com.yy.miaosha.result.CodeMsg;
import com.yy.miaosha.result.Result;
import com.yy.miaosha.service.GoodsService;
import com.yy.miaosha.service.MiaoshaService;
import com.yy.miaosha.service.OrderService;
import com.yy.miaosha.vo.GoodsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-26 18:13
 **/
@Service
public class MQReceiver {
    @Resource
    RedisService redisService;
    @Resource
    GoodsService goodsService;
    @Resource
    OrderService orderService;
    @Resource
    MiaoshaService miaoshaService;

    public static Logger log = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receiveMiaoMessage(String miaoshaMessage) {
        MiaoshaMessage mm = redisService.stringToBean(miaoshaMessage, MiaoshaMessage.class);
        MiaoshaUser miaoshaUser = mm.getMiaoshaUser();
        long goodsId = mm.getGoodsId();


        //通过Id来获取秒杀商品对象，并再一次判断库存，这里是查数据库了，因为能进队列的很少，所以这里不用担心数据库的压力
        GoodsVO goodsVO = goodsService.getGoodsByGoodsId(goodsId);
        int stock = goodsVO.getStockCount();
        if (stock <= 0) {
            return;
        }
        //再一次判断是否已经秒杀到了,即不能重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(miaoshaUser.getId(), goodsId);
        if (order != null) {
            return;
        }

        //真正秒杀
        miaoshaService.miaosha(miaoshaUser, goodsVO);




    }







    //下面所有方法都是测试时候用的，与项目无关
    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        System.out.println("receive message:"+message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopicQueue1(String message) {
        System.out.println("receive message from topicQueue1:"+message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopicQueue2(String message) {
        System.out.println("receive message from topicQueue2:"+message);
    }

    @RabbitListener(queues = MQConfig.HEADERS_QUEUE)
    public void receiveHeadersQueue(byte[] message) {  //因为发送给交换机的也是字节数组
        System.out.println("receive message from HeadersQueue:"+new String(message));
    }
}