package com.example.Cart.Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {
    private String secret = "newSecretKey";

    public Claims parseToken(String token) {
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
