package com.example.Cart.DTO;

import com.example.Cart.Entity.OrderItem;
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
public class OrderResponseDTO {
    @Id
    private int orderId;

    private List<OrderItemResponseDTO> orderItems = new ArrayList<>();

    private long totalPrice;
}
