package com.zyh.lightquestionserver.controller;

import com.zyh.lightquestionserver.dao.UserDao;
import com.zyh.lightquestionserver.entity.Result;
import com.zyh.lightquestionserver.entity.User;
import com.zyh.lightquestionserver.entity.UserClient;
import com.zyh.lightquestionserver.server.RedisService;
import com.zyh.lightquestionserver.server.SMSConfigService;
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

    /**
     * 登录
     * @param user
     * @return
     */
    @PostMapping("/login")
    public User login(@RequestBody User user) {
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("phone", user.getPhone());
        List<User> users = userDao.selectByMap(tempMap);
        if(users.size() == 0) {
            //查询失败，说明用户尚未注册
            //注册
            log.info("注册");
            user.setDate(new Date());   //设置注册时间
            userDao.insert(user);       //插入
        }
        //发送验证码，并存在Redis里保持5分钟
        smsConfigService.sendSMVCode(user.getPhone());
        return user;
    }

    /**
     * 检查验证码
     * @param map
     * @return
     */
    @PostMapping("/login/code")
    public Object loginCode(@RequestBody Map<String,String> map) {
        if(redisService.get(map.get("phone")) == null) {
            //redis里没有验证码，验证码已失效
            return 1;
        } else if(redisService.get(map.get("phone")).equals(map.get("code"))) {
            //验证码一致
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("phone", map.get("phone"));
            List<User> users = userDao.selectByMap(tempMap);
            String token = JWTUtil.createToken(users.get(0));
            //key：电话号码  value：Token
            redisService.set(map.get("phone"), token);
            UserClient userClient = new UserClient();
            userClient.setUsername(users.get(0).getUsername());
            userClient.setDate(users.get(0).getDate());
            userClient.setId(users.get(0).getId());
            userClient.setToken(token);
            return userClient;
        } else {
            //验证码不一致
            return 2;
        }
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
