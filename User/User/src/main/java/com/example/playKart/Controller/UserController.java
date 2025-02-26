package com.example.playKart.Controller;


import com.example.playKart.DTO.*;
import com.example.playKart.Entity.Address;
import com.example.playKart.Entity.User;
import com.example.playKart.Services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {


    @Autowired
    UserServices userService;

    @GetMapping("/")
    public List<ResponseUserDTO> getAllUsers(@RequestHeader("Authorization") String authHeader){

        String token = authHeader.substring(7);
        return userService.getAllUsers(token);
    }
    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO userLoginDto){

        return userService.loginUser(userLoginDto);
    }
    @PostMapping("register")
    public ResponseEntity<ResponseUserDTO> register(@Valid @RequestBody RegisterDTO userRegisterDto){
        return new ResponseEntity<>(userService.registerUser(userRegisterDto),HttpStatus.CREATED);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUserDTO> getUserDetails(@PathVariable Integer userId){

        ResponseUserDTO responseUserDTO = userService.getUserDetails(userId);
        return new ResponseEntity<>(responseUserDTO,HttpStatus.OK);
    }

    @PostMapping("/address/")
    public ResponseEntity<AddressDTO> addAddress(@Valid @RequestBody AddressDTO addressdto,@RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        AddressDTO createdAddress = userService.addAddress(addressdto,token);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping("/address/")
    public ResponseEntity<List<AddressDTO>> getUserAddresses(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        List<AddressDTO> addresses = userService.getUserAddresses(token);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PatchMapping("/")
    public ResponseEntity<ResponseUserDTO> updateUserProfile(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody UpdateUserDTO updateUserDTO){
        String token = authHeader.substring(7);
        ResponseUserDTO responseUserDTO = userService.updateUserProfile(token,updateUserDTO);
        return new ResponseEntity<>(responseUserDTO,HttpStatus.CREATED);

    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Integer id,@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        userService.deleteAddress(id,token);
        return new ResponseEntity<>("Address Removed Successfully",HttpStatus.NO_CONTENT);
    }





}
