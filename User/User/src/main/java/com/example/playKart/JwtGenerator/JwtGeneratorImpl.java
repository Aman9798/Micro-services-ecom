package com.example.playKart.JwtGenerator;


import com.example.playKart.Entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtGeneratorImpl implements JwtGeneratorInterface{

    @Value("${app.jwttoken.message}")
    private String message;
    @Override
    public Map<String, String> generateToken(User user) {
      //  System.out.println("Secret: " + secret);
        String jwtToken="";
        String secret = "newSecretKey";
        jwtToken = Jwts.builder().setSubject(user.getUserId().toString()).setIssuedAt(new Date()).claim("isAdmin", user.isAdmin()).signWith(SignatureAlgorithm.HS256, secret).compact();
        Map<String, String> jwtTokenGen = new HashMap<>();
        jwtTokenGen.put("token", jwtToken);
        jwtTokenGen.put("message", message);
        return jwtTokenGen;
    }
}