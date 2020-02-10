package com.yy.miaosha.vo;

import com.yy.miaosha.domain.Goods;

import java.util.Date;

/**
 * @program: miaosha
 * @description: 结合了普通商品和秒杀商品的信息
 * @author: yangyun86
 * @create: 2020-02-09 18:08
 **/
public class GoodsVO extends Goods {
    private Integer stockCount;
    private Double miaoshaPrice;
    private Date startDate;
    private Date endDate;

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getMiaoshaPrice() {
        return miaoshaPrice;
    }

    public void setMiaoshaPrice(Double miaoshaPrice) {
        this.miaoshaPrice = miaoshaPrice;
    }
}