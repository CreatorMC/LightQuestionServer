package com.zyh.lightquestionserver.utils;

import com.zyh.lightquestionserver.entity.User;
import io.jsonwebtoken.*;

import java.util.Objects;
import java.util.UUID;

public class JWTUtil {

    private static final long time = 1000*60*60*24;                     //有效时间
    private static final String signature = "SDZFXYwlkjaqxyTMS4548";    //签名

    /**
     * 创建Token
     * @param user 载荷内容
     * @return jwtToken
     */
    public static String createToken(User user) {
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                //header    头部
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                //payload   载荷
                .claim("id",String.valueOf(user.getId()))
                //主题
                .setSubject("admin")
                //有效期（现在被Redis管理，token中不需要设置有效期）
//                .setExpiration(new Date(System.currentTimeMillis() + time))
                .setId(UUID.randomUUID().toString())
                //signature签名
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
        return jwtToken;
    }

    /**
     * 检查Token是否有效
     * @param token token
     * @return id   用户ID
     */
    public static String checkToken(String token){
        if (Objects.equals(token, "") || token == null){
            return null;
        }

        Jws<Claims> claimsJws = null;
        try {
            //解析后拿到存有token信息的集合
            claimsJws = Jwts.parser().setSigningKey(signature).parseClaimsJws(token);
        } catch (Exception e) {
            return null;
        }
        return (String) claimsJws.getBody().get("id");
    }

}
