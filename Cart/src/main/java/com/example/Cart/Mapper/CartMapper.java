package com.example.Cart.Mapper;

import com.example.Cart.DTO.CartDTO;
import com.example.Cart.Entity.Cart;

public class CartMapper {

    public static CartDTO convertToCartDTO(Cart cart) {
        return CartDTO.builder()
                .cartId(cart.getCartId())
                .cartItems(cart.getCartItems())
                .totalPrice(cart.getTotalPrice())
                .build();
    }
}
