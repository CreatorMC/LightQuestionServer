package com.zyh.lightquestionserver.server.impl;

import com.zyh.lightquestionserver.annotation.NeedEncrypt;
import com.zyh.lightquestionserver.dao.UserDao;
import com.zyh.lightquestionserver.entity.*;
import com.zyh.lightquestionserver.server.RedisService;
import com.zyh.lightquestionserver.server.SMSConfigService;
import com.zyh.lightquestionserver.server.UserEncryptServer;
import com.zyh.lightquestionserver.server.UserServer;
import com.zyh.lightquestionserver.utils.JWTUtil;
import com.zyh.lightquestionserver.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
@Slf4j
public class UserServerImpl implements UserServer {

    @Autowired
    RedisService redisService;
    @Autowired
    UserDao userDao;
    @Autowired
    SMSConfigService smsConfigService;

    @Autowired
    JavaMailSenderImpl mailSender;

    @Autowired
    UserEncryptServer userEncryptServer;        //加密操作User

    @Value("${spring.mail.username}")
    String emailServer;

    @Override
    public String loginServer(User user) {
        Map<String, Object> tempMap = new HashMap<>();
        user = userEncryptServer.encryptUser(user);     //拿到被加密后的user，可以跟数据库内数据进行比对
        tempMap.put("phone", user.getPhone());
        List<User> users = userDao.selectByMap(tempMap);
        if (users.size() == 0) {
            //查询失败，说明用户尚未注册
            //注册
            user.setUsername("QTX"+RandomUtil.randomDigitNumber(10));
            user.setDate(new Date());                   //设置注册时间
            userDao.insert(user);                       //插入
        }
        //发送验证码，并存在Redis里保持5分钟
        String uuid = UUID.randomUUID().toString();
        smsConfigService.sendSMVCode(uuid);
        return uuid;

    }

    @Override
    public Object loginCodeServer(UserVCode userVCode) {
        if(redisService.get(userVCode.getUuid()) == null) {
            //redis里没有验证码，验证码已失效
            return 1;
        } else if(redisService.get(userVCode.getUuid()).equals(userVCode.getCode())) {
            //验证码一致
            Map<String, Object> tempMap = new HashMap<>();
            userVCode = userEncryptServer.encryptUser(userVCode);
            tempMap.put("phone", userVCode.getPhone());
            List<User> users = userDao.selectByMap(tempMap);
            return getUserClient(users);
        } else {
            //验证码不一致
            return 2;
        }

    }

    /**
     * 邮箱登录
     * @param userEmailRegister 包含邮箱和密码
     * @return UserClient / null
     */
    @Override
    @NeedEncrypt
    public UserClient loginEmailServer(UserEmailRegister userEmailRegister) {
        List<User> users = userDao.selectByMap(new HashMap<String, Object>() {{
            put("email", userEmailRegister.getEmail());
        }});
        if(users.size() == 0) {
            //此用户未注册
            return null;
        }
        if(userEmailRegister.getPassword().equals(users.get(0).getPassword())) {
            //密码正确
            return getUserClient(users);
        }
        //密码不对
        return null;
    }

    /**
     * 发送邮箱验证码
     * @param userEmailRegister
     * @return UUID: 成功发送邮件, "-1": 邮箱已被注册, "-2": 邮件发送异常
     */
    @Override
    public String sendEmailVCode(UserEmailRegister userEmailRegister) {
        try {
            String email = userEmailRegister.getEmail();    //未被加密的邮箱
            UserEmailRegister finalUserEmailRegister = userEncryptServer.encryptUser(userEmailRegister);
            List<User> emailList = userDao.selectByMap(new HashMap<String, Object>() {{
                put("email", finalUserEmailRegister.getEmail());
            }});
            if(emailList.size() > 0) {
                //说明此邮箱被注册过了
                return "-1";
            }
            //生成验证码
            String codeNum = RandomUtil.generateVCode();
            String uuid = UUID.randomUUID().toString();


            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            //标题
            helper.setSubject("【轻题侠】您的验证码为："+codeNum);
            //内容
            helper.setText("【轻题侠】您的验证码为："+"<h2>"+codeNum+"</h2>"+"5分钟内有效，请勿告诉他人！",true);
            //邮件接收者
            helper.setTo(email);
            //邮件发送者，必须和配置文件里的一样，不然授权码匹配不上
            helper.setFrom(emailServer);
            mailSender.send(mimeMessage);

            //验证码存储在redis里5分钟
            redisService.set(uuid, codeNum, 5 * 60 * 1000L);

            return uuid;
        } catch (MessagingException e) {
            return "-2";
        }
    }

    /**
     * 用邮箱注册用户
     *
     * @param userEmailRegisterVCode
     * @return
     */
    @Override
    public String registerEmail(UserEmailRegisterVCode userEmailRegisterVCode) {
        String vCode = redisService.get(userEmailRegisterVCode.getUuid());
        if(Objects.isNull(vCode)) {
            return "-2";    //验证码过期
        }
        if(userEmailRegisterVCode.getVCode().equals(vCode)) {
            //验证码相等
            User user = new User();
            user.setEmail(userEmailRegisterVCode.getEmail());
            user.setPassword(userEmailRegisterVCode.getPassword());
            user.setUsername("QTX"+RandomUtil.randomDigitNumber(10));
            user.setDate(new Date());
            userEncryptServer.insertUser(user);
            return "0";
        }
        return "-1";    //验证码输入错误
    }

    /**
     * 得到UserClient
     * @param users 用户列表
     * @return UserClient
     */
    @NotNull
    private UserClient getUserClient(List<User> users) {
        String token = JWTUtil.createToken(users.get(0));
        //key：用户ID  value：Token
        redisService.set(String.valueOf(users.get(0).getId()), token);
        UserClient userClient = new UserClient();
        userClient.setUsername(users.get(0).getUsername());
        userClient.setDate(users.get(0).getDate());
        userClient.setId(users.get(0).getId());
        userClient.setToken(token);
        return userClient;
    }
}
