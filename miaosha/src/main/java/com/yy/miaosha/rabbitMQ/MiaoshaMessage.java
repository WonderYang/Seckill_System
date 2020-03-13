package com.yy.miaosha.rabbitMQ;

import com.yy.miaosha.domain.MiaoshaGoods;
import com.yy.miaosha.domain.MiaoshaUser;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-03-08 13:28
 **/
public class MiaoshaMessage {
    private MiaoshaUser miaoshaUser;
    private long goodsId;

    public MiaoshaUser getMiaoshaUser() {
        return miaoshaUser;
    }

    public void setMiaoshaUser(MiaoshaUser miaoshaUser) {
        this.miaoshaUser = miaoshaUser;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}