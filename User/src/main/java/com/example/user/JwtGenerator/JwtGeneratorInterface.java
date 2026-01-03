package com.example.user.JwtGenerator;

import com.example.user.Entity.User;

import java.util.Map;

public interface JwtGeneratorInterface {
    Map<String, String> generateToken(User user);
}
