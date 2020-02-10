package com.yy.miaosha.config;

import com.yy.miaosha.domain.MiaoshaUser;
import com.yy.miaosha.service.MiaoshaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: miaoshaha
 * @description:
 * @author: yangyun86
 * @create: 2020-02-08 18:47
 **/
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Resource
    MiaoshaUserService miaoshaUserService;

    //这个方法代表在Controller中的入参中有MiaoshaUser这个参数才去执行resolveArgument这个方法；
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> cla =  methodParameter.getParameterType();
        return cla == MiaoshaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) nativeWebRequest.getNativeRequest();
        HttpServletResponse response = (HttpServletResponse) nativeWebRequest.getNativeResponse();
        String paramToken = request.getParameter(miaoshaUserService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request, miaoshaUserService.COOKIE_NAME_TOKEN);
        if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            return "login";
        }
        String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
        return miaoshaUserService.getByToken(response, token);

    }

    /**
     * 自己写的工具方法，从前端带来的Cookie中（许多）获取指定的cookie；
     * @param request
     * @param cookieName
     * @return
     */
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies) {
            if(cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}