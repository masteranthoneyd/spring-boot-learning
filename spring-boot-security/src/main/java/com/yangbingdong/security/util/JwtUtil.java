package com.yangbingdong.security.util;

import cn.hutool.json.JSONUtil;
import com.yangbingdong.security.config.entity.SecurityUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.experimental.UtilityClass;

import java.util.Date;

/**
 * @author <a href="mailto:yangbingdong1994@gmail.com">yangbingdong</a>
 * @since
 */
@UtilityClass
public class JwtUtil {

    final long expire = 10 * 60 * 1000L;
    public final String secret = "To be a god";

    public static String createAccessToken(SecurityUser securityUser){
        return Jwts.builder()
                   .setId(securityUser.getUserId().toString())
                   .setSubject(securityUser.getUsername())
                   .setIssuedAt(new Date())
                   .setIssuer("YourGod")
                   .claim("authorities", JSONUtil.toJsonStr(securityUser.getAuthorities()))
                   .setExpiration(new Date(System.currentTimeMillis() + expire))
                   .signWith(SignatureAlgorithm.HS512, secret)
                   .compact();
    }
}
