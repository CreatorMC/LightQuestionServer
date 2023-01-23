package com.zyh.lightquestionserver.aop;

import com.zyh.lightquestionserver.annotation.EncryptField;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class EncryptAspect {

    @Autowired
    private StandardPBEStringEncryptor standardPBEStringEncryptor;

    @Pointcut("@annotation(com.zyh.lightquestionserver.annotation.NeedEncrypt)")
    public void pointCut() {}                   //AOP切点

    @Around("pointCut()")                       //环绕通知
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        encrypt(joinPoint);
        return joinPoint.proceed();             //执行原方法
    }

    public void encrypt(ProceedingJoinPoint joinPoint)  {
        Object[] objects;
        try {
            objects = joinPoint.getArgs();
            if (objects.length != 0) {
                for (Object object : objects) {
                    encryptObject(object);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密对象
     * @param obj
     * @throws IllegalAccessException
     */
    private void encryptObject(Object obj) throws IllegalAccessException {

        if (Objects.isNull(obj)) {
            log.info("当前需要加密的object为null");
            return;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            //检测到当前是需要加密的字段
            boolean containEncryptField = field.isAnnotationPresent(EncryptField.class);
            //获取访问权
            field.setAccessible(true);
            Object ready = field.get(obj);
            //此字段不为null才加密
            if (containEncryptField && !Objects.isNull(ready)) {
                String value = standardPBEStringEncryptor.encrypt(String.valueOf(ready));
                field.set(obj, value);
            }
        }
    }

}
