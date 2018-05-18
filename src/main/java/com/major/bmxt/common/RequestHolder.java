package com.major.bmxt.common;

import com.major.bmxt.model.TbUser;

import javax.servlet.http.HttpServletRequest;

/**
 * 高并发下可以处理各自的数据, 优化
 */
public class RequestHolder {

    private static final ThreadLocal<TbUser> userHolder = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void add(TbUser user) {
        userHolder.set(user);
    }

    public static void add(HttpServletRequest request) {
        requestHolder.set(request);
    }

    public static TbUser getCurrentUser() {
        return userHolder.get();
    }

    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }

    public static void remove() {
        userHolder.remove();
        requestHolder.remove();
    }
}
