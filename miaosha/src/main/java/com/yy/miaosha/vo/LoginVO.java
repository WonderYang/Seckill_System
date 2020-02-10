package com.yy.miaosha.vo;

import com.yy.miaosha.validator.IsMobile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-04 16:06
 **/
public class LoginVO {
    //使用JSR303进行参数校验，如果校验失败则会抛出BindException，此时异常捕捉器（GlobalExceptionHandler）就会扑捉到该异常并进行对应处理
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginVO{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}