package com.major.bmxt.interceptor;

import com.major.bmxt.common.CookieSessionManage;
import com.major.bmxt.common.RequestHolder;
import com.major.bmxt.model.TbUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        String uri = httpServletRequest.getRequestURI();
        // 不拦截静态文件
        if(uri.contains("/bmxtPage/static") || uri.contains("login.html") || uri.contains("/user/")) {
            return true;
        }
        // 判断用户是否已经登录
        TbUser user = (TbUser) CookieSessionManage.getSession(httpServletRequest);
        if(user != null) {
            RequestHolder.add(user);
            RequestHolder.add(httpServletRequest);
            return true;
        }
        httpServletResponse.sendRedirect("http://39.107.252.145/bmxt/bmxtPage/html/login.html");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        RequestHolder.remove();
    }
}
