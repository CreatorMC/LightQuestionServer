package com.zyh.lightquestionserver.controller;

import com.zyh.lightquestionserver.entity.Result;
import com.zyh.lightquestionserver.entity.User;
import com.zyh.lightquestionserver.server.UserServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    UserServer userServer;

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        return userServer.loginServer(user);
    }

    /**
     * 检查验证码
     * @param map
     * @return
     */
    @PostMapping("/login/code")
    public Object loginCode(@RequestBody Map<String,String> map) {
        return userServer.loginCodeServer(map);
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
