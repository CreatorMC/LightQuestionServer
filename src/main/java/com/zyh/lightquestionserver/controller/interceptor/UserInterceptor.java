package com.zyh.lightquestionserver.controller.interceptor;

import com.zyh.lightquestionserver.server.RedisService;
import com.zyh.lightquestionserver.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果请求为 OPTIONS 请求，则返回 true,否则需要通过jwt验证
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())){
            return true;
        }
        String token = request.getHeader("Authorization");
        String id = JWTUtil.checkToken(token);
        //1.判断请求是否有效
        if(id == null) {
            //前端的localStorage里没有用户信息
            response.sendError(403);
            return false;
        }
        String value = redisService.get(id);
        if (value == null || !value.equals(token)) {
            response.sendError(403);
            return false;
        }

        //2.判断是否需要续期，离过期时间只有一天时才更新
        if (redisService.getExpireTime(id) < 60 * 60 * 24) {
            redisService.set(id, token);
        }
        return true;
    }
}
