package com.example.Cart.Mapper;

import com.example.Cart.DTO.OrderItemResponseDTO;
import com.example.Cart.DTO.OrderResponseDTO;
import com.example.Cart.Entity.OrderItem;
import com.example.Cart.Entity.Orders;

import java.util.List;

public class OrderMapper {

    public static OrderResponseDTO convertToOrderResponseDTO(Orders order) {
        List<OrderItemResponseDTO> orderItems = order.getOrderItems().stream()
                .map(OrderMapper::convertToOrderItemResponseDTO)
                .toList();

        return OrderResponseDTO.builder()
                .orderId(order.getOrderId())
                .orderItems(orderItems)
                .totalPrice(order.getTotalPrice())
                .build();
    }

    public static OrderItemResponseDTO convertToOrderItemResponseDTO(OrderItem orderItem) {
        return OrderItemResponseDTO.builder()
                .orderItemId(orderItem.getOrderItemId())
                .createdAt(orderItem.getCreatedAt())
                .productId(orderItem.getProductId())
                .productName(orderItem.getProductName())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .userId(orderItem.getUserId())
                .userName(orderItem.getUserName())
                .Address(orderItem.getAddress())
                .phoneNumber(orderItem.getPhoneNumber())
                .imageURL(orderItem.getImageURL())
                .build();
    }
}
