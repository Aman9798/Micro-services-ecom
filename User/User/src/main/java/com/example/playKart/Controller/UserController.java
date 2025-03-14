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

import javax.xml.bind.SchemaOutputResolver;
import java.util.List;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Autowired
    UserServices userService;

    @GetMapping("/")
    public List<ResponseUserDTO> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        return userService.getAllUsers(token);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO userLoginDto) {
        System.out.println(userLoginDto.getEmail());
        return userService.loginUser(userLoginDto);
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO userRegisterDto) {
        return new ResponseEntity<>(userService.registerUser(userRegisterDto), HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<ResponseUserDTO> getUserDetails(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        ResponseUserDTO responseUserDTO = userService.getUserDetails(token);
        return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUserDTO> getUserById(@PathVariable int userId) {

        ResponseUserDTO responseUserDTO = userService.getUserDetailsById(userId);
        return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
    }

    @PostMapping("/address/")
    public ResponseEntity<Address> addAddress(@Valid @RequestBody AddressDTO addressdto, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Address createdAddress = userService.addAddress(addressdto, token);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping("/address/")
    public ResponseEntity<List<Address>> getUserAddresses(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        List<Address> addresses = userService.getUserAddresses(token);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PatchMapping("/")
    public ResponseEntity<ResponseUserDTO> updateUserProfile(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
        String token = authHeader.substring(7);
       // System.out.println(updateUserDTO.getName());
        ResponseUserDTO responseUserDTO = userService.updateUserProfile(token, updateUserDTO);
        return new ResponseEntity<>(responseUserDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        userService.deleteAddress(id, token);
        return new ResponseEntity<>("Address Removed Successfully", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Integer id, @RequestHeader("Authorization") String authHeader) {
      //  System.out.println("1");
        String token = authHeader.substring(7);
        Address userAddress = userService.getAddressById(id, token);
        return new ResponseEntity<>(userAddress, HttpStatus.OK);
    }

    @PatchMapping("/makeAdmin")
    public ResponseEntity<ResponseUserDTO> makeUserAdmin(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody AdminRequestDTO adminRequestDTO){
        String token = authHeader.substring(7);
        ResponseUserDTO user = userService.makeUserAdmin(adminRequestDTO,token);
        return new ResponseEntity<>(user,HttpStatus.OK);

    }
}

