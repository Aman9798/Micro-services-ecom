package com.example.Cart.Controller;


import com.example.Cart.DTO.CartItemDTO;
import com.example.Cart.Entity.Orders;
import com.example.Cart.Services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order/")
public class OrderController {


    @Autowired
    private OrderService orderServices;
    @GetMapping
    public ResponseEntity<Orders> getAllOrders(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);

        Orders userOrders = orderServices.getAllOrders(token);
        return new ResponseEntity<>(userOrders, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<Orders> placeOrder(@RequestHeader("Authorization") String authHeader, @RequestBody List<CartItemDTO> cartItems){
        String token = authHeader.substring((7));

        Orders userOrders = orderServices.placeOrder(token,cartItems);
        return new ResponseEntity<>(userOrders,HttpStatus.OK);
    }




}
