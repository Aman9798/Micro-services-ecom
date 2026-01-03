package com.example.user.Mapper;

import com.example.user.DTO.ResponseUserDTO;
import com.example.user.Entity.User;

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
