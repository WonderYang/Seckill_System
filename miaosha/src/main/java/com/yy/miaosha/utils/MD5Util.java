package com.yy.miaosha.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-04 11:29
 **/
public class MD5Util {

    private static final String salt = "1a2b3c4d";

    public static String md5(String str) {
        return DigestUtils.md5Hex(str);
    }

    public static String inputPassToDBPass(String input, String saltDB) {
        String formPass = inputPassFormPass(input);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    /**
     * md5第一次加密：即将前端
     * @param inputPass
     * @return
     */
    public static String inputPassFormPass(String inputPass) {
        //System.out.println(salt.charAt(2));
        //这里有一个大坑，必须加上""，不然结果是不对的
        String str = ""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        //System.out.println(str);
        return md5(str);
    }

    /**
     * md5第二次加密：这个加密后是要存储到数据库的
     * @param formPass
     * @param salt
     * @return
     */
    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static void main(String[] args) {
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));
    }
}