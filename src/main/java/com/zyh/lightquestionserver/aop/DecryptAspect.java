package com.zyh.lightquestionserver.aop;

import com.zyh.lightquestionserver.annotation.EncryptField;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
@Aspect
@Component
public class DecryptAspect {

    @Qualifier("standardPBEStringEncryptor")
    @Autowired
    private StringEncryptor stringEncryptor;

    @Pointcut("@annotation(com.zyh.lightquestionserver.annotation.NeedDecrypt)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        //解密
        Object result = decrypt(joinPoint);
        return result;
    }

    public Object decrypt(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            Object obj = joinPoint.proceed();
            if (obj != null) {
                //抛砖引玉 ，可自行扩展其他类型字段的判断
                if (obj instanceof String) {
                    decryptValue(obj);
                } else {
                    result = decryptData(obj);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }

    private Object decryptData(Object obj) throws IllegalAccessException {

        if (Objects.isNull(obj)) {
            return null;
        }
        if (obj instanceof ArrayList) {
            decryptList(obj);
        } else {
            decryptObj(obj);
        }


        return obj;
    }

    /**
     * 针对单个实体类进行 解密
     * @param obj
     * @throws IllegalAccessException
     */
    private void decryptObj(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean hasSecureField = field.isAnnotationPresent(EncryptField.class);
            if (hasSecureField) {
                field.setAccessible(true);
                String realValue = (String) field.get(obj);
                String value = stringEncryptor.decrypt(realValue);
                field.set(obj, value);
            }
        }
    }

    /**
     * 针对list<实体来> 进行反射、解密
     * @param obj
     * @throws IllegalAccessException
     */
    private void decryptList(Object obj) throws IllegalAccessException {
        List<Object> result = new ArrayList<>();
        if (obj instanceof ArrayList) {
            for (Object o : (List<?>) obj) {
                result.add(o);
            }
        }
        for (Object object : result) {
            decryptObj(object);
        }
    }


    public String decryptValue(Object realValue) {
        try {
            realValue = stringEncryptor.encrypt(String.valueOf(realValue));
        } catch (Exception e) {
            log.info("解密异常={}", e.getMessage());
        }
        return String.valueOf(realValue);
    }


}
