package com.zyh.lightquestionserver.config;

import com.zyh.lightquestionserver.controller.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .excludePathPatterns("/User/logout/**")         //排除退出登录请求
                .excludePathPatterns("/User/login/**")          //排除登录请求
                .excludePathPatterns("/User/loginEmail/**")     //排除邮箱登录请求
                .excludePathPatterns("/User/register/**")       //排除邮箱注册请求
                .excludePathPatterns("/User/registerCode/**")   //排除邮箱验证码请求
                .addPathPatterns("/**");                        //其余所有的请求
    }
}
