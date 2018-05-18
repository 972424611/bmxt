package com.major.bmxt.mapper;

import com.major.bmxt.model.TbUser;

public interface UserMapper {

    void insertUser(TbUser user);

    TbUser selectUserByAdmAndPw(TbUser user);

    TbUser selectUserByUsername(String username);
}
