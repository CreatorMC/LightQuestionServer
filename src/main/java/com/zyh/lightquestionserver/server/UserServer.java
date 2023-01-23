package com.zyh.lightquestionserver.server;

import com.zyh.lightquestionserver.entity.*;

public interface UserServer {
    String loginServer(User user);
    Object loginCodeServer(UserVCode userVCode);
    String sendEmailVCode(UserEmailRegister userEmailRegister);
    String registerEmail(UserEmailRegisterVCode userEmailRegisterVCode);
    UserClient loginEmailServer(UserEmailRegister userEmailRegister);
}

