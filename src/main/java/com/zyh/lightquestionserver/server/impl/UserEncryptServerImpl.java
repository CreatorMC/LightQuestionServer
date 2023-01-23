package com.zyh.lightquestionserver.server.impl;

import com.zyh.lightquestionserver.annotation.NeedEncrypt;
import com.zyh.lightquestionserver.dao.UserDao;
import com.zyh.lightquestionserver.entity.User;
import com.zyh.lightquestionserver.entity.UserEmailRegister;
import com.zyh.lightquestionserver.entity.UserVCode;
import com.zyh.lightquestionserver.server.UserEncryptServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserEncryptServerImpl implements UserEncryptServer {

    @Autowired
    UserDao userDao;

    /**
     * 加密后插入用户
     * @param user
     */
    @NeedEncrypt    //需要加密（自定义注解）
    @Override
    public void insertUser(User user) {
        userDao.insert(user);
    }

    @NeedEncrypt
    @Override
    public User encryptUser(User user) {
        return user;
    }

    @NeedEncrypt
    @Override
    public UserVCode encryptUser(UserVCode user) {
        return user;
    }

    @NeedEncrypt
    @Override
    public UserEmailRegister encryptUser(UserEmailRegister userEmailRegister) {
        return userEmailRegister;
    }
}
