package com.zyh.lightquestionserver.controller;

import com.zyh.lightquestionserver.dao.UserFeedBackDao;
import com.zyh.lightquestionserver.entity.*;
import com.zyh.lightquestionserver.server.UserServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    UserServer userServer;
    @Autowired
    UserFeedBackDao userFeedBackDao;

    /**
     * 登录
     * @param user
     * @return {
     *     uuid: "UUID"
     * }
     */
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        return new HashMap<String, String>(){{
            put("uuid", userServer.loginServer(user));
        }};
    }

    /**
     * 邮箱登录
     * @param userEmailRegister
     * @return UserClient / null
     */
    @PostMapping("/loginEmail")
    public UserClient loginEmail(@RequestBody UserEmailRegister userEmailRegister) {
        return userServer.loginEmailServer(userEmailRegister);
    }

    /**
     * 邮箱注册
     * @param userEmailRegister
     * @return UUID: 成功发送邮件, "-1": 邮箱已被注册, "-2": 邮件发送异常
     */
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody UserEmailRegister userEmailRegister) {
        String uuid = userServer.sendEmailVCode(userEmailRegister);
        Map<String, String> map = new HashMap<>();
        map.put("uuid", uuid);
        return map;
    }

    /**
     * 验证邮箱验证码
     * @param userEmailRegisterVCode
     * @return Result {
     *     resultString: "0": 成功 "-1": 验证码不对 "-2": 验证码过期
     * }
     */
    @PostMapping("/registerCode")
    public Result registerCode(@RequestBody UserEmailRegisterVCode userEmailRegisterVCode) {
        Result result = new Result();
        result.setResultString(userServer.registerEmail(userEmailRegisterVCode));
        return result;
    }

    /**
     * 检查验证码
     * @param userVCode
     * @return
     */
    @PostMapping("/login/code")
    public Object loginCode(@RequestBody UserVCode userVCode) {
        return userServer.loginCodeServer(userVCode);
    }

    /**
     * 检查是否能保持登录（拦截器里处理了）
     * @return
     */
    @GetMapping("/check")
    public Result check() {
        return Result.returnSuccessResult();
    }

    /**
     * 用户反馈
     * @param userFeedBack 反馈信息
     * @return Result
     */
    @PostMapping("/feedback")
    public Result feedBack(@RequestBody UserFeedBack userFeedBack) {
        userFeedBackDao.insert(userFeedBack);
        return Result.returnSuccessResult();
    }

}
