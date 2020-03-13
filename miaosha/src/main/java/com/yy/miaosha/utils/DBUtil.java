package com.yy.miaosha.utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * @program: miaosha
 * @description:用作测试的，用来获取配置文件中数据库的连接信息
 * @author: yangyun86
 * @create: 2020-02-12 19:20
 **/
public class DBUtil {
    private static Properties props;

    static {
        try {
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("application.properties");
            props = new Properties();
            props.load(in);
            in.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() throws Exception{
        String url = props.getProperty("spring.datasource.url");
        String username = props.getProperty("spring.datasource.username");
        String password = props.getProperty("spring.datasource.password");
        String driver = props.getProperty("spring.datasource.driver-class-name");
        Class.forName(driver);
        return DriverManager.getConnection(url,username, password);
    }
}