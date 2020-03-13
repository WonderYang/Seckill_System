package com.yy.miaosha.service;

import com.yy.miaosha.dao.GoodsDao;
import com.yy.miaosha.domain.Goods;
import com.yy.miaosha.domain.MiaoshaGoods;
import com.yy.miaosha.domain.MiaoshaOrder;
import com.yy.miaosha.vo.GoodsVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-09 18:15
 **/
@Service
public class GoodsService {
    @Resource
    GoodsDao goodsDao;

    public List<GoodsVO> listGoodVO() {
        return goodsDao.getListGoodsVO();
    }

    public GoodsVO getGoodsByGoodsId(long goodsId) {
        return goodsDao.getGoodsVOByGoodId(goodsId);
    }

    public boolean reduceStock(GoodsVO goodsVO) {
        MiaoshaGoods miaoshaGoods = new MiaoshaGoods();
        miaoshaGoods.setGoodsId(goodsVO.getId());
        int res = goodsDao.reduceStock(miaoshaGoods);
        return res > 0 ? true : false;
    }

}