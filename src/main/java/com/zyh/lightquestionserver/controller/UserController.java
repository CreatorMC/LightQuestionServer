package com.zyh.lightquestionserver.controller;

import com.zyh.lightquestionserver.entity.User;
import com.zyh.lightquestionserver.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/User")
public class UserController {

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        log.info(user.toString());
        if("admin".equals(user.getUsername()) && "123".equals(user.getPassword())) {
            user.setToken(JWTUtil.createToken("", user.getUsername()));
            log.info(user.toString());
            return user;
        }
        return null;
    }
}
