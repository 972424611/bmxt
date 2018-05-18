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
        String uri = httpServletRequest.getRequestURI();
        if(uri.contains("/bmxt/static") || uri.contains("login.html") || uri.contains("/user/")) {
            return true;
        }
        TbUser user = (TbUser) CookieSessionManage.getSession(httpServletRequest);
        if(user != null) {
            RequestHolder.add(user);
            RequestHolder.add(httpServletRequest);
            return true;
        }
        httpServletResponse.sendRedirect("/bmxt/bmxt/html/login.html");
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
