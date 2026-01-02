package com.example.Cart.Mapper;

import com.example.Cart.DTO.OrdersDTO;
import com.example.Cart.Entity.Orders;

public class OrderMapper {

    public static OrdersDTO convertToOrderDTO(Orders order) {
        return OrdersDTO.builder()
                .orderId(order.getOrderId())
                .orderItems(order.getOrderItems())
                .totalPrice(order.getTotalPrice())
                .build();
    }
}
