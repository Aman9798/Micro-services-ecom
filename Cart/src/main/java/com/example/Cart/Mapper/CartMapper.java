package com.example.Cart.Mapper;

import com.example.Cart.DTO.CartResponseDTO;
import com.example.Cart.DTO.CartItemResponseDTO;
import com.example.Cart.Entity.Cart;
import com.example.Cart.Entity.CartItem;

import java.util.List;

public class CartMapper {

    public static CartResponseDTO convertToCartResponseDTO(Cart cart) {

        List<CartItemResponseDTO> cartItemsResponse = cart.getCartItems().stream()
                .map(CartMapper::convertToCartItemResponseDTO)
                .toList();

        return CartResponseDTO.builder()
                .cartId(cart.getCartId())
                .cartItems(cartItemsResponse)
                .totalPrice(cart.getTotalPrice())
                .build();
    }

    public static CartItemResponseDTO convertToCartItemResponseDTO(CartItem cartItem) {
        return  CartItemResponseDTO.builder()
                .cartItemId(cartItem.getCartItemId())
                .productName(cartItem.getProductName())
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getPrice())
                .imageUrl(cartItem.getImageUrl())
                .build();
    }
}
