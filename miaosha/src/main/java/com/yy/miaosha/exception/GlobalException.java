package com.yy.miaosha.exception;

import com.yy.miaosha.result.CodeMsg;

/**
 * @program: miaosha
 * @description: 自定义异常类，代表一些业务之中的错误；
 * @author: yangyun86
 * @create: 2020-01-31 13:13
 **/
public class GlobalException extends RuntimeException{

	private static final long serialVersionUID = -822406955351983065L;

	private CodeMsg cm;
	
	public GlobalException(CodeMsg cm) {
		super(cm.toString());
		this.cm = cm;
	}

	public CodeMsg getCm() {
		return cm;
	}

}
