package com.program.service;

import com.program.entity.User;
import com.program.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 注册用户
     * @param user
     */
    @Override
    public void register(User user) {
        //1.根据用户名查询数据库中是否存在相同用户名
        User userDB = userMapper.findByUserName(user.getUsername());

        //2.判断用户是否存在
        if (!ObjectUtils.isEmpty(userDB))
            throw new RuntimeException("The user already exists!");

        //3.注册用户
        //  密码加密
        String encPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPassword(encPassword);
        userMapper.save(user);
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     * 由于密码是明文，无法与数据库中的密码比对
     * 所以只能先通过用户名寻找
     * 再将用户输入的密码加密后与数据库中的比对
     */    
    @Override
    public User login(String username, String password) {

        //1.根据用户名查询用户
        User user = userMapper.findByUserName(username);
        if (ObjectUtils.isEmpty(user))
            throw new RuntimeException("Username is Incorrect!");

        //2.比较密码
        String encPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!user.getPassword().equals(encPassword))
            throw new RuntimeException("Password is incorrect!");
        return user;
    }
}
