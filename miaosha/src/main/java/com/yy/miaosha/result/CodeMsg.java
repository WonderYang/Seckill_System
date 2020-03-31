package com.yy.miaosha.result;

/**
 * @program: miaosha
 * @description:
 * @author: yangyun86
 * @create: 2020-01-31 13:13
 **/
public class CodeMsg {
    private int code;
    private String msg;

    //通用异常5001XX
    public static CodeMsg SUCCESS = new CodeMsg(0,"success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100,"服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg REQUEST_ILLEGLE = new CodeMsg(500102, "请求非法");

    //登陆模块5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210,"Session失效或者不存在");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211,"密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212,"手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213,"手机号格式不正确");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214,"手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215,"密码错误");


    //商品模块5003XX

    //订单模块5004XX
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500400,"订单不存在");


    //秒杀模块5005XX
    public static CodeMsg MIAOSHA_OVER = new CodeMsg(500501,"商品已经被抢空啦");
    public static CodeMsg MIAOSHA_REPEAT = new CodeMsg(500502,"秒杀商品不能重复秒杀");
    public static CodeMsg MIAOSHA_FAIL = new CodeMsg(500503,"秒杀失败，服务异常");

    public CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //用来填充错误信息的，比如上面的500101异常
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}