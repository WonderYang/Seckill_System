package com.yy.miaosha.service;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import com.yy.miaosha.dao.OrderDao;
import com.yy.miaosha.domain.MiaoshaGoods;
import com.yy.miaosha.domain.MiaoshaOrder;
import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.domain.OrderInfo;
import com.yy.miaosha.redis.RedisService;
import com.yy.miaosha.redis.prefix.OrderKey;
import com.yy.miaosha.vo.GoodsVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-10 14:55
 **/
@Service
public class OrderService {
    @Resource
    OrderDao orderDao;
    @Resource
    RedisService redisService;
    public MiaoshaOrder getMiaoshaOrderByUserIdAndGoodsId(long userId, long goodsId) {
        return redisService.get(OrderKey.getMiaoshaOrderByUidGid, userId+"_"+goodsId, MiaoshaOrder.class);
    }

    @Transactional
    public OrderInfo createOrder(MiaoshaUser user, GoodsVO goodsVO) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goodsVO.getId());
        orderInfo.setGoodsName(goodsVO.getGoodsName());
        orderInfo.setGoodsPrice(goodsVO.getMiaoshaPrice());
        orderInfo.setOrderChannel(1); //下单渠道
        orderInfo.setStatus(0);   //0代表未支付
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goodsVO.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);
        //redisService.set(OrderKey.getMiaoshaOrderByUidGid, ""+user.getId()+"_"+goodsVO.getId(), miaoshaOrder);

        //存到redis，下次访问是否重复秒杀时直接访问redis（有效期为1小时），效率更高
        redisService.set(OrderKey.getMiaoshaOrderByUidGid, user.getId()+"_"+goodsVO.getId(), miaoshaOrder);
        return orderInfo;
    }

    /**
     * 根据orderId查询
     * @param id
     * @return
     */
    public OrderInfo getOrderById(long id) {
        OrderInfo orderInfo = orderDao.getOrderById(id);
        return orderInfo;
    }
}