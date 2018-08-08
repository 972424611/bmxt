package com.major.bmxt.mapper;

import com.major.bmxt.model.TbUser;

public interface UserMapper {

    /**
     * 添加用户
     * @param user
     */
    void insertUser(TbUser user);

    /**
     * 获取用户(用来判断帐号密码是否正确)
     * @param user
     * @return TbUser
     */
    TbUser selectUserByAdmAndPw(TbUser user);

}
