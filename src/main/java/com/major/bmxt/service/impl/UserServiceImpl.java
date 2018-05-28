package com.major.bmxt.service.impl;

import com.major.bmxt.common.CookieSessionManage;
import com.major.bmxt.exception.UserException;
import com.major.bmxt.mapper.UserMapper;
import com.major.bmxt.model.TbUser;
import com.major.bmxt.param.UserParam;
import com.major.bmxt.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void saveUser(UserParam user) {
        TbUser tbUser = new TbUser();
        BeanUtils.copyProperties(user, tbUser);
        userMapper.insertUser(tbUser);
    }

    @Override
    public String verifyUser(UserParam user, HttpServletRequest request) {
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        TbUser tbUser = new TbUser();
        BeanUtils.copyProperties(user, tbUser);
        tbUser = userMapper.selectUserByAdmAndPw(tbUser);
        if(tbUser == null) {
            throw new UserException("账号密码不正确");
        }
        tbUser.setPassword(null);
        CookieSessionManage.setSession(request, tbUser);
        return tbUser.getProvince();
    }
}
