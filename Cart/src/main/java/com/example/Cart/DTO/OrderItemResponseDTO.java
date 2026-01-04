package com.example.Cart.DTO;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
public class OrderItemResponseDTO {
    @Id
    private int orderItemId;

    private Date createdAt;

    private int productId;

    private String productName;

    private Long price;

    private int quantity;

    private int userId;

    private String userName;

    private String Address;

    private String phoneNumber;

    private String imageURL;

    public Long getTotalPrice() {
        return price * quantity;
    }
}
