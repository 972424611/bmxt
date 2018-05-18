package com.major.bmxt.service;

import com.major.bmxt.param.UserParam;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    /**
     * 保存用户
     * @param user UserParam
     */
    void saveUser(UserParam user);

    /**
     * 检测用户信息
     * @param user UserParam
     * @param request HttpServletRequest
     * @return province 省份
     */
    String verifyUser(UserParam user, HttpServletRequest request);
}
