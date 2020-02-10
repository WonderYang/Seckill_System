package com.yy.miaosha.exception;

import com.yy.miaosha.result.CodeMsg;
import com.yy.miaosha.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @program: miaosha
 * @description:这个类是当后端出现规定的异常时，并不会直接给前端报错，而是返回对应的错误信息；
 * @author: yangyun86
 * @create: 2020-01-31 13:13
 **/
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
	//这个注解代表，只要是Exception，都会被这里拦截，然后由此出处理；
	@ExceptionHandler(value=Exception.class)
	public Result<String> exceptionHandler(HttpServletRequest request, Exception e){
		e.printStackTrace();
		//GlobalException是我们的自定义异常
		if(e instanceof GlobalException) {
			GlobalException ex = (GlobalException)e;
			return Result.error(ex.getCm());
			//BindException异常是参数校验错误时产生的异常（JSR303那部分）
		}else if(e instanceof BindException) {
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
			return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
		}else {
			return Result.error(CodeMsg.SERVER_ERROR);
		}
	}
}
