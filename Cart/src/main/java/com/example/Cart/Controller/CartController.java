package com.example.Cart.Controller;

import com.example.Cart.DTO.CartDTO;
import com.example.Cart.DTO.CartItemDTO;
import com.example.Cart.DTO.UpdateProductDTO;
import com.example.Cart.Entity.CartItem;
import com.example.Cart.Services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;



    @PostMapping("/")
    public ResponseEntity<List<CartItem>> addCartItem(@Valid @RequestBody CartItemDTO cartItem, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);

        List<CartItem> cartItems = cartService.addCartItem(cartItem,token);
        return new ResponseEntity<>(cartItems,HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<CartDTO> getCart(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        CartDTO cart = cartService.getCart(token);
        return new ResponseEntity<>(cart,HttpStatus.OK);
    }


    @PatchMapping("/{cartItemId}")
    public ResponseEntity<CartDTO> updateCartItemQuantity(@PathVariable int cartItemId, @RequestHeader("Authorization") String authHeader, @RequestBody UpdateProductDTO updateProductDTO) {
        String token = authHeader.substring(7);
        CartDTO updatedCart = cartService.updateCartItem(cartItemId,token,updateProductDTO.getQuantity());
        return new ResponseEntity<>(updatedCart,HttpStatus.CREATED);
    }






}