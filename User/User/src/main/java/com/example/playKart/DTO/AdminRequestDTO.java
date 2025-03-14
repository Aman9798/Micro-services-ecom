package com.example.playKart.DTO;

import jakarta.validation.constraints.NotNull;

public class AdminRequestDTO {


    @NotNull
    private String userEmail;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
