package com.example.playKart.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterDTO {

    @NotNull(message = "Name can't be empty")
    private String name;
    @NotNull(message= "email can't be empty")
    @Email(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message ="please provide a valid email address")
    private String email;
    @NotNull(message= "Password can't be empty")
    @Pattern(regexp ="(?=.*\\d.*)(?=.*[a-zA-Z].*)(?=.*[!#\\$@%&\\?].*).{8,20}",message = "password must contain 1 lowercase,1 uppercase and 1 special character and ust be of min. length 8")
    private String password;
    @NotNull(message = "Phone Number can't be empty")
    @Size(min = 10, max= 10,message ="phone number should contain 10 numbers")
    private String phoneNumber;


    public String getName() {
        return name;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

}
