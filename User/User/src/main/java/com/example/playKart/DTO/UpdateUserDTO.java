package com.example.playKart.DTO;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUserDTO {

    private String name;

    @Size(min = 10, max= 10,message ="phone number should contain 10 numbers")
    private String phoneNumber;
    @Pattern(regexp ="(?=.*\\d.*)(?=.*[a-zA-Z].*)(?=.*[!#\\$@%&\\?].*).{8,20}",message = "password must contain 1 lowercase,1 uppercase and 1 special character and ust be of min. length 8")
    private String password;


    public String getName(){
        return name;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getPassword(){
        return password;
    }


}
