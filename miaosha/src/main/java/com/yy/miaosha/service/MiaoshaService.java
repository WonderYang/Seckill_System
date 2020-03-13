package com.yy.miaosha.service;

import com.yy.miaosha.domain.MiaoshaOrder;
import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.domain.OrderInfo;
import com.yy.miaosha.redis.RedisService;
import com.yy.miaosha.redis.prefix.MiaoshaKey;
import com.yy.miaosha.vo.GoodsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-10 15:15
 **/
@Service
public class MiaoshaService {
    @Resource
    GoodsService goodsService;
    @Resource
    OrderService orderService;
    @Resource
    RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVO goods) {

        //1. 减库存
        boolean res = goodsService.reduceStock(goods);
        if (res) {
            OrderInfo orderInfo = orderService.createOrder(user, goods);
            //2. 生成新订单并返回
            return orderInfo;
        }else {
            //给Redis中添加一个该商品已卖完的标记
            setGoodsOver(goods.getId());
            return null;
        }
    }

    public long getMiaoshaResult(long userId, long goodsId) {
        MiaoshaOrder miaoshaOrder = orderService.getMiaoshaOrderByUserIdAndGoodsId(userId, goodsId);
        if (miaoshaOrder != null) {
            //秒杀成功
            return miaoshaOrder.getOrderId();
        }else {
            //判断是不是已经卖完了，卖完了就不需要再轮询了，直接返回-1，前端识别到-1就不会轮询了；
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            }else {
                //代表商品没卖完，还没秒杀到，这种情况就是还在排队中，前端识别到0后会继续轮询；
                return 0;
            }
        }
    }

    /**
     * 判断某个商品是否还有库存
     * @param
     * @return
     */
    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId);
    }

}