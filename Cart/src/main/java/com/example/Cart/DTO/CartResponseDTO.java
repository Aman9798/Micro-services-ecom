package com.example.Cart.DTO;

import com.example.Cart.Entity.CartItem;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
public class CartResponseDTO {
    @Id
    private int cartId;

    private List<CartItemResponseDTO> cartItems = new ArrayList<>();

    private long totalPrice;
}
