package com.zyh.lightquestionserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy     //开启AOP
public class LightQuestionServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightQuestionServerApplication.class, args);
    }

}
