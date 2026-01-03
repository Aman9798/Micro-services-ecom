package com.example.user.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2IiwiaWF0IjoxNzQxOTMyNTcwLCJpc0FkbWluIjp0cnVlfQ.ag2sp-3lVSomqhMqn1XotEGz5hMB92TEG2QiHDMOdzo";
    String token1 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNCIsImlhdCI6MTc0MTkzMjc3MSwiaXNBZG1pbiI6ZmFsc2V9.2zBB7iIzMp9Qje7ogFTm4yqwK3k8NjIMVuFwfXncnso";

    @Test
    void registerUser() {
    }

    @Test
    void getAllUsers() {

        assertNotNull(userService.getAllUsers(token1));
    }

    @Test
    void loginUser() {
    }

    @Test
    void getUserDetails() {
    }

    @Test
    void addAddress() {
    }

    @Test
    void getUserAddresses() {
    }

    @Test
    void updateUserProfile() {
    }

    @Test
    void deleteAddress() {
    }

    @Test
    void getAddressById() {
    }

    @Test
    void getUserDetailsById() {
    }

    @Test
    void makeUserAdmin() {
    }
}