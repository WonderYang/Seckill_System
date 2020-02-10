package com.yy.miaosha.dao;

import com.yy.miaosha.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsDao {

    //左连接，显示a表中存在的全部数据及a、b中都有的数据，a中有、b中没有的数据以null显示
    @Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVO> getListGoodsVO();

}
