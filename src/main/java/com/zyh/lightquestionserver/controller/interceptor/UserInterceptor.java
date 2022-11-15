package com.zyh.lightquestionserver.controller.interceptor;

import com.zyh.lightquestionserver.server.RedisService;
import com.zyh.lightquestionserver.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authToken = request.getHeader("Authorization");
        String token = authToken.substring("Bearer".length() + 1).trim();
        String userId = JWTUtil.checkToken(token);
        //1.判断请求是否有效
        if (redisService.get(userId) == null || !redisService.get(userId).equals(token)) {
            return false;
        }

        //2.判断是否需要续期，离过期时间只有一天时才更新
        if (redisService.getExpireTime(userId) < 60 * 60 * 24) {
            redisService.set(userId, token);
        }
        return true;
    }
}
