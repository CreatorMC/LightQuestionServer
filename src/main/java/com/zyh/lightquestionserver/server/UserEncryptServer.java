package com.zyh.lightquestionserver.server;

import com.zyh.lightquestionserver.entity.User;
import com.zyh.lightquestionserver.entity.UserEmailRegister;
import com.zyh.lightquestionserver.entity.UserVCode;

public interface UserEncryptServer {
    public void insertUser(User user);
    public User encryptUser(User user);
    UserVCode encryptUser(UserVCode user);
    UserEmailRegister encryptUser(UserEmailRegister userEmailRegister);
    User decryptUser(User user);
}
