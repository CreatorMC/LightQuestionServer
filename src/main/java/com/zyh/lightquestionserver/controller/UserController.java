package com.zyh.lightquestionserver.controller;

import com.zyh.lightquestionserver.entity.Result;
import com.zyh.lightquestionserver.entity.User;
import com.zyh.lightquestionserver.server.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    RedisService redisService;

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        if("admin".equals(user.getUsername()) && "123".equals(user.getPassword())) {
//            user.setToken(JWTUtil.createToken("", user.getUsername()));
            return user;
        }
        return null;
    }

    /**
     * 检查是否能保持登录（拦截器里处理了）
     * @return
     */
    @GetMapping("/check")
    public Result check() {
        return Result.returnSuccessResult();
    }

}
