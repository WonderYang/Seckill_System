package com.yy.miaosha.vo;

import com.yy.miaosha.domain.MiaoshaUser;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-20 10:25
 **/
public class GoodsDetailVO {
    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVO goods ;
    private MiaoshaUser user;

    public int getMiaoshaStatus() {
        return miaoshaStatus;
    }
    public void setMiaoshaStatus(int miaoshaStatus) {
        this.miaoshaStatus = miaoshaStatus;
    }
    public int getRemainSeconds() {
        return remainSeconds;
    }
    public void setRemainSeconds(int remainSeconds) {
        this.remainSeconds = remainSeconds;
    }
    public GoodsVO getGoods() {
        return goods;
    }
    public void setGoods(GoodsVO goods) {
        this.goods = goods;
    }
    public MiaoshaUser getUser() {
        return user;
    }
    public void setUser(MiaoshaUser user) {
        this.user = user;
    }
}