package com.example.Cart.Controller;


import com.example.Cart.DTO.AddressRequestDTO;
import com.example.Cart.DTO.BuyNowRequestDTO;
import com.example.Cart.DTO.CartItemRequestDTO;
import com.example.Cart.DTO.OrderResponseDTO;
import com.example.Cart.Service.OrderService;
import com.example.Cart.Utils.JwtTokenUtil;
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

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllUserOrders(@RequestHeader("Authorization") String authHeader){
        String token = jwtTokenUtil.getToken(authHeader);

        List<OrderResponseDTO> userOrders = orderServices.getAllOrders(token);
        return new ResponseEntity<>(userOrders, HttpStatus.OK);
    }

    @GetMapping("{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@RequestHeader("Authorization") String authHeader, @PathVariable int orderId){
        String token = jwtTokenUtil.getToken(authHeader);

        OrderResponseDTO userOrder = orderServices.getOrderById(orderId, token);
        return new ResponseEntity<>(userOrder, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<OrderResponseDTO>> placeOrder(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody AddressRequestDTO addressRequestDTO){
        List<OrderResponseDTO> userOrders = orderServices.placeOrder(addressRequestDTO, authHeader);
        return new ResponseEntity<>(userOrders,HttpStatus.CREATED);
    }

    @PostMapping("instant")
    public ResponseEntity<List<OrderResponseDTO>> buyNow(@RequestHeader("Authorization") String authHeader, @Valid @RequestBody BuyNowRequestDTO buyNowRequestDTO){

        List<OrderResponseDTO> userOrders = orderServices.buyNow(buyNowRequestDTO, authHeader);
        return new ResponseEntity<>(userOrders,HttpStatus.CREATED);
    }

    @GetMapping("product/{productId}")
    public boolean userHasOrder(@RequestHeader("Authorization") String authHeader, @PathVariable int productId){
        String token = jwtTokenUtil.getToken(authHeader);
        return orderServices.checkUserHasBoughtProduct(productId, token);
    }
}
