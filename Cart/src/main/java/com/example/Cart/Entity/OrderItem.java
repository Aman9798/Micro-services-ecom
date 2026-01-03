package com.example.Cart.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Orders order;

    public Long getTotalPrice() {
        return price * quantity;
    }

}
