package com.example.Cart.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemResponseDTO {
    @Id
    private int cartItemId;

    private String productName;

    private int productId;

    private int quantity;

    private long price;

    private String imageUrl;

    public long getTotalPrice() {
        return price * quantity;
    }
}
