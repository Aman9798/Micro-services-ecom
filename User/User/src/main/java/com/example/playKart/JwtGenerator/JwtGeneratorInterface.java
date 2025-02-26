package com.example.playKart.JwtGenerator;

import com.example.playKart.Entity.User;

import java.util.Map;

public interface JwtGeneratorInterface {
    Map<String, String> generateToken(User user);
}
