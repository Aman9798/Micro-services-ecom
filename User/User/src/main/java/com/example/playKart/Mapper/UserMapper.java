package com.example.playKart.Mapper;

import com.example.playKart.DTO.ResponseUserDTO;
import com.example.playKart.Entity.User;

public class UserMapper {

    public static ResponseUserDTO convertToResponseUserDTO(User user) {
        return ResponseUserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .isAdmin(user.isAdmin())
                .build();
    }
}
