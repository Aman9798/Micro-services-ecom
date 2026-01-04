package com.example.Cart.Controller;

import com.example.Cart.DTO.CartResponseDTO;
import com.example.Cart.DTO.CartItemRequestDTO;
import com.example.Cart.DTO.CartItemResponseDTO;
import com.example.Cart.DTO.UpdateCartItemQuantityDTO;
import com.example.Cart.Service.CartService;
import com.example.Cart.Utils.JwtTokenUtil;
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

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/")
    public ResponseEntity<List<CartItemResponseDTO>> addCartItem(@Valid @RequestBody CartItemRequestDTO cartItemRequest, @RequestHeader("Authorization") String authHeader) {
        String token = jwtTokenUtil.getToken(authHeader);
        List<CartItemResponseDTO> cartItems = cartService.addCartItem(cartItemRequest, token);
        return new ResponseEntity<>(cartItems, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<CartResponseDTO> getCart(@RequestHeader("Authorization") String authHeader) {
        String token = jwtTokenUtil.getToken(authHeader);
        CartResponseDTO cart = cartService.getCart(token);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PatchMapping("/{cartItemId}")
    public ResponseEntity<CartResponseDTO> updateCartItemQuantity(@PathVariable int cartItemId, @RequestHeader("Authorization") String authHeader, @RequestBody UpdateCartItemQuantityDTO updateCartItemQuantityDTO) {
        String token = jwtTokenUtil.getToken(authHeader);
        CartResponseDTO updatedCart = cartService.updateCartItem(cartItemId, updateCartItemQuantityDTO.getQuantity(), token);
        return new ResponseEntity<>(updatedCart, HttpStatus.CREATED);
    }
}