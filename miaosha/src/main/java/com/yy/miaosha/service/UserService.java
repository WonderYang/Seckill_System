package com.yy.miaosha.service;

import com.yy.miaosha.dao.UserDao;
import com.yy.miaosha.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-01-31 18:35
 **/

@Service
public class UserService {

    @Resource
    UserDao userDao;

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    //测试事物，即插入已经存在的主键，看事物能不能回滚；如果没有@Transactional注解，下面的user1是会插入成功的；
    @Transactional
    public boolean tx() {
        User user1 = new User();
        user1.setId(2);
        user1.setName("Alice");
        userDao.insert(user1);

        //数据库已经存在id为1的数据，所以这个事物会回滚，即上面的user1也会插入失败；
        User user2 = new User();
        user2.setId(1);
        user2.setName("Nike");
        userDao.insert(user2);

        return true;

    }
}