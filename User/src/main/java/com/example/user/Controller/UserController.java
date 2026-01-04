package com.example.user.Controller;


import com.example.user.DTO.*;
import com.example.user.Entity.Address;
import com.example.user.Service.UserService;
import com.example.user.Utils.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping("/")
    public List<ResponseUserDTO> getAllUsers(@RequestHeader("Authorization") String authHeader) {
        String token = jwtTokenUtil.getToken(authHeader);
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
        String token = jwtTokenUtil.getToken(authHeader);
        ResponseUserDTO responseUserDTO = userService.getUserDetails(token);
        return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUserDTO> getUserById(@PathVariable int userId) {

        ResponseUserDTO responseUserDTO = userService.getUserDetailsById(userId);
        return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
    }

    @PostMapping("/address/")
    public ResponseEntity<AddressResponseDTO> addAddress(@Valid @RequestBody AddressRequestDTO addressRequest, @RequestHeader("Authorization") String authHeader) {
        String token = jwtTokenUtil.getToken(authHeader);
        AddressResponseDTO createdAddress = userService.addAddress(addressRequest, token);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping("/address/")
    public ResponseEntity<List<AddressResponseDTO>> getUserAddresses(@RequestHeader("Authorization") String authHeader) {
        String token = jwtTokenUtil.getToken(authHeader);
        List<AddressResponseDTO> addresses = userService.getUserAddresses(token);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }

    @PatchMapping("/")
    public ResponseEntity<ResponseUserDTO> updateUserProfile(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody UpdateUserDTO updateUser) {
        String token = jwtTokenUtil.getToken(authHeader);

        ResponseUserDTO responseUserDTO = userService.updateUserProfile(token, updateUser);
        return new ResponseEntity<>(responseUserDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Integer addressId, @RequestHeader("Authorization") String authHeader) {
        String token = jwtTokenUtil.getToken(authHeader);
        userService.deleteAddress(addressId, token);
        return new ResponseEntity<>("Address Removed Successfully", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/address/{addressId}")
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable Integer addressId, @RequestHeader("Authorization") String authHeader) {
        String token = jwtTokenUtil.getToken(authHeader);
        AddressResponseDTO userAddress = userService.getAddressById(addressId, token);
        return new ResponseEntity<>(userAddress, HttpStatus.OK);
    }

    @PatchMapping("/makeAdmin")
    public ResponseEntity<ResponseUserDTO> makeUserAdmin(@RequestHeader("Authorization") String authHeader,@Valid @RequestBody AdminRequestDTO adminRequest){
        String token = jwtTokenUtil.getToken(authHeader);
        ResponseUserDTO user = userService.makeUserAdmin(adminRequest,token);
        return new ResponseEntity<>(user,HttpStatus.OK);

    }
}

