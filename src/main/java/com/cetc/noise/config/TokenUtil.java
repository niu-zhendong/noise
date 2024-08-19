package com.cetc.noise.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Date;

@Component
public class TokenUtil{

    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);
    private static final String SECRET = "tuoFngKeJi";

    //创建一个jwt密钥 加密和解密都需要用这个玩意
    private static final SecretKey key = Jwts.SIG.HS256.key()
            .random(new SecureRandom(SECRET.getBytes(StandardCharsets.UTF_8)))
            .build();

    /**
     * 生成token
     *
     * @param username 用户名
     * @return token
     */
    public static String createToken(String username,String password,Date expiration) {
        JwtBuilder builder = Jwts.builder();
        Date now = new Date();
        // 生成token
        builder.id(password)
                .issuer("GuiYangGngTaiHanBao") //签发者
                .subject(username) //主题
                .issuedAt(now) //签发时间
                .expiration(expiration)
                .signWith(key); //签名方式
        return builder.compact();
    }

    /**
     * 解析token
     *
     * @param token jwt token
     * @return Claims
     */
    public Claims claims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .clock(DefaultClock.INSTANCE)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw e;
        }
    }
}
