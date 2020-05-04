package com.gp.webdriverapi.common.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Map;

/**
 * Jwt工具类
 *
 * @author Aim-Trick
 * @date 2020/4/8
 */
public class JwtUtils {

    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateKey(String username, Map<String, Object> map) {
        String jws = Jwts
                .builder()
                .setClaims(map)
                .setSubject(username)
                .signWith(key)
                .compact();
        return jws;
    }

}
