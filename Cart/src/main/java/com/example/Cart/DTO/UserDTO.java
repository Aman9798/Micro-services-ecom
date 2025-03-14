package com.example.Cart.DTO;

public class UserDTO {


    private String name;

    public UserDTO(String name, String email, String phoneNumber,boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
    }

    private String email;
    private String phoneNumber;

    private boolean isAdmin;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }




}
