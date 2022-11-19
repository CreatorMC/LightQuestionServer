package com.zyh.lightquestionserver.controller;

import com.zyh.lightquestionserver.dao.UserDao;
import com.zyh.lightquestionserver.entity.Result;
import com.zyh.lightquestionserver.entity.User;
import com.zyh.lightquestionserver.entity.UserClient;
import com.zyh.lightquestionserver.server.RedisService;
import com.zyh.lightquestionserver.server.SMSConfigService;
import com.zyh.lightquestionserver.server.UserServer;
import com.zyh.lightquestionserver.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    RedisService redisService;
    @Autowired
    UserDao userDao;
    @Autowired
    SMSConfigService smsConfigService;
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
