package com.yy.miaosha.dao;


import com.yy.miaosha.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    //这里使用的@Select注解，就不用写对应的xml文件了；
    @Select("select * from user where id = #{id}")
    public User getUserById(@Param("id") int id);

    @Insert("insert into user(id,name) values(#{id},#{name})")
    public int insert(User user);
}
