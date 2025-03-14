package com.example.Cart.Controller;


import com.example.Cart.DTO.AddressRequestDTO;
import com.example.Cart.DTO.CartItemDTO;
import com.example.Cart.DTO.OrdersDTO;
import com.example.Cart.Services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order/")
@CrossOrigin
public class OrderController {


    @Autowired
    private OrderService orderServices;
    @GetMapping
    public ResponseEntity<List<OrdersDTO>> getAllUserOrders(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);

        List<OrdersDTO> userOrders = orderServices.getAllOrders(token);
        return new ResponseEntity<>(userOrders, HttpStatus.OK);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<OrdersDTO> getOrderById(@RequestHeader("Authorization") String authHeader,@PathVariable int orderId){
        String token = authHeader.substring(7);

        OrdersDTO userOrder = orderServices.getOrderById(orderId,token);
        return new ResponseEntity<>(userOrder, HttpStatus.OK);
    }



    @PostMapping
    public ResponseEntity<List<OrdersDTO>> placeOrder(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody AddressRequestDTO addressRequestDTO){

        List<OrdersDTO> userOrders = orderServices.placeOrder(authHeader,addressRequestDTO);
        return new ResponseEntity<>(userOrders,HttpStatus.CREATED);
    }

    @PostMapping("instant")
    public ResponseEntity<List<OrdersDTO>> buyNow(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody CartItemDTO cartItemDTO){

        List<OrdersDTO> userOrders = orderServices.buyNow(authHeader,cartItemDTO);
        return new ResponseEntity<>(userOrders,HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public boolean userHasOrder(@RequestHeader("Authorization") String authHeader,@PathVariable int productId){

        System.out.println(1);
        String token = authHeader.substring(7);
        return orderServices.checkUserHasBoughtProduct(token,productId);
    }




}
