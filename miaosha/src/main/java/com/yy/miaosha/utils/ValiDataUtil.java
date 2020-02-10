package com.yy.miaosha.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: miaosha
 * @description:验证数据格式的工具类
 * @author: yangyun86
 * @create: 2020-02-05 20:39
 **/
public class ValiDataUtil {
    public static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");
    public static boolean isMobile(String str) {
        if(StringUtils.isEmpty(str)) {
            return false;
        }

        Matcher m = mobile_pattern.matcher(str);
        return m.matches();
    }

//    public static void main(String[] args) {
//        System.out.println(isMobile("12345678911"));
//        System.out.println(isMobile("123456789"));
//
//    }

}