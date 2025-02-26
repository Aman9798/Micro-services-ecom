package com.example.playKart.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    public Claims parseToken(String token) {
        String secret = "newSecretKey";
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isAdmin(String token) {
        Claims claims = parseToken(token);
        return claims.get("isAdmin", Boolean.class);
    }

    public String getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.getSubject();
    }


}
