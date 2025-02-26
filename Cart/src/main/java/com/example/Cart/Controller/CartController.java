package com.example.Cart.Controller;

import com.example.Cart.DTO.CartItemDTO;
import com.example.Cart.Entity.Cart;
import com.example.Cart.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;



    @PostMapping("/")
    public ResponseEntity<Cart> addCartItem(@RequestBody CartItemDTO cartItem,@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);

        Cart cart = cartService.addCartItem(cartItem,token);
        return new ResponseEntity<>(cart,HttpStatus.CREATED);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Cart> removeCartItem( @PathVariable int cartItemId,@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Cart updatedCart = cartService.removeCartItem(cartItemId,token);
        return new ResponseEntity<>(updatedCart,HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Cart> getCart(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Cart cart = cartService.getCart(token);
        return new ResponseEntity<>(cart,HttpStatus.OK);
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<Cart> updateCartItemQuantity(@PathVariable int cartItemId,@RequestParam int quantity,@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Cart updatedCart = cartService.updateCartItem(cartItemId,token,quantity);
        return new ResponseEntity<>(updatedCart,HttpStatus.CREATED);
    }






}