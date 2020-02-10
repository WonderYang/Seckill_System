package com.yy.miaosha.result;


import java.io.Serializable;

public class Result<T> implements Serializable {
    private int code;
    private String msg;
    private T data;

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    //成功时调用的构造
    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }
    //失败时调用的构造，需要传一个CodeMsg类
    private Result(CodeMsg codeMsg) {
        //防止空指针异常
        if(codeMsg == null) {
            return;
        }
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }

    //范型方法的范型与范型类的范型参数无关哟！
    //成功时返回
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    //失败时返回
    public static <T> Result<T> error(CodeMsg codeMsg) {
        return new Result<>(codeMsg);

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
