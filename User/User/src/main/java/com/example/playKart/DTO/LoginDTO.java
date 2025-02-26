package com.example.playKart.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class LoginDTO {

    private String email;

    private String password;


    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

}
