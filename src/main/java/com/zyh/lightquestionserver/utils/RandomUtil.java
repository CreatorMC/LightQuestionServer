package com.zyh.lightquestionserver.utils;

import java.util.Random;

public class RandomUtil {
    private static final String DEFAULT_DIGITS = "0";
    private static final String FIRST_DEFAULT_DIGITS = "1";

    public static String makeUpNewData(String target, int length){
        return makeUpNewData(target, length, DEFAULT_DIGITS);
    }

    public static String makeUpNewData(String target, int length, String add){
        if(target.startsWith("-")) target.replace("-", "");
        if(target.length() >= length) return target.substring(0, length);
        StringBuffer sb = new StringBuffer(FIRST_DEFAULT_DIGITS);
        for (int i = 0; i < length - (1 + target.length()); i++) {
            sb.append(add);
        }
        return sb.append(target).toString();
    }

    /**
     * 生产一个随机的指定位数的字符串数字
     * @param length
     * @return
     */
    public static String randomDigitNumber(int length){
        long start = Long.parseLong(makeUpNewData("", length));//1000+8999=9999
        long end = Long.parseLong(makeUpNewData("", length + 1)) - start;//9000
        return (long)(Math.random() * end) + start + "";
    }

    /**
     * 生成验证码
     * @return
     */
    public static String generateVCode() {
        StringBuilder codeNum = new StringBuilder();
        int [] code = new int[3];
        Random random = new Random();
        //自动生成验证码
        for (int i = 0; i < 6; i++) {
            int num = random.nextInt(10) + 48;
            int uppercase = random.nextInt(26) + 65;
            int lowercase = random.nextInt(26) + 97;
            code[0] = num;
            code[1] = uppercase;
            code[2] = lowercase;
            codeNum.append((char) code[random.nextInt(3)]);
        }
        return codeNum.toString();
    }

}
