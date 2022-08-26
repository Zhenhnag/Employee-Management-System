package com.program.mapper;

import com.program.entity.User;

public interface UserMapper {

    //根据用户名查询用户
    User findByUserName(String username);

    //保存用户信息
    void save(User user);
}
