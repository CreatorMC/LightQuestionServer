package com.zyh.lightquestionserver.server.impl;

import com.zyh.lightquestionserver.dao.UserDao;
import com.zyh.lightquestionserver.entity.User;
import com.zyh.lightquestionserver.entity.UserClient;
import com.zyh.lightquestionserver.server.RedisService;
import com.zyh.lightquestionserver.server.SMSConfigService;
import com.zyh.lightquestionserver.server.UserServer;
import com.zyh.lightquestionserver.utils.JWTUtil;
import com.zyh.lightquestionserver.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServerImpl implements UserServer {

    @Autowired
    RedisService redisService;
    @Autowired
    UserDao userDao;
    @Autowired
    SMSConfigService smsConfigService;

    @Override
    public User loginServer(User user) {
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.put("phone", user.getPhone());
        List<User> users = userDao.selectByMap(tempMap);
        if (users.size() == 0) {
            //查询失败，说明用户尚未注册
            //注册
            user.setUsername("QTX"+RandomUtil.randomDigitNumber(10));
            user.setDate(new Date());   //设置注册时间
            userDao.insert(user);       //插入
        }
        //发送验证码，并存在Redis里保持5分钟
        smsConfigService.sendSMVCode(user.getPhone());
        return user;

    }

    @Override
    public Object loginCodeServer(Map<String, String> map) {
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

}
