package com.major.bmxt.controller;

import com.major.bmxt.beans.ResultData;
import com.major.bmxt.common.CookieSessionManage;
import com.major.bmxt.exception.UserException;
import com.major.bmxt.param.UserParam;
import com.major.bmxt.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户controller
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(value = "/login")
    public ResultData login(UserParam userParam, HttpServletRequest request) {
        String errorMsg;
        if(StringUtils.isBlank(userParam.getUsername())) {
            errorMsg = "帐户名不能为空";
        }else if(StringUtils.isBlank(userParam.getPassword())) {
            errorMsg = "密码不能为空";
        }else {
            try {
                String province = userService.verifyUser(userParam, request);
                return ResultData.success(province);
            } catch (UserException e) {
                return ResultData.fail(e.getMessage());
            }
        }
        return ResultData.fail(errorMsg);
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        CookieSessionManage.clearCookieAndSession(request, response);
        return "redirect:http://39.107.252.145/bmxt/bmxtPage/html/login.html";
    }
}
