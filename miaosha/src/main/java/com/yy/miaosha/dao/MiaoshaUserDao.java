package com.yy.miaosha.dao;

import com.yy.miaosha.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiaoshaUserDao {

    //id为手机号
    @Select("select * from miaosha_user where id=#{id}")
    public MiaoshaUser getById(@Param("id") long id);
}
