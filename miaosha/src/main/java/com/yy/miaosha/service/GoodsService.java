package com.yy.miaosha.service;

import com.yy.miaosha.dao.GoodsDao;
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
}