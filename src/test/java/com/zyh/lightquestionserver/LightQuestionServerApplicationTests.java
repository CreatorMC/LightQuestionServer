package com.zyh.lightquestionserver;

import com.zyh.lightquestionserver.annotation.NeedEncrypt;
import com.zyh.lightquestionserver.entity.User;
import com.zyh.lightquestionserver.server.UserEncryptServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest
@Slf4j
class LightQuestionServerApplicationTests {

    @Autowired
    UserEncryptServer userEncryptServer;

    @Test
    public void test(){
        User user = new User();
        user.setPhone("123456789");
        user.setPassword("AWFDHFs465a4das54d56fds65");
        user.setEmail("FSGDGHF@qq.com");
        userEncryptServer.encryptUser(user);
        log.info("加密后的User：" + user);
        userEncryptServer.decryptUser(user);
        log.info("解密后的User：" + user);
    }
}
