package com.example.Cart.Entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    @OneToMany(mappedBy = "order",orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();

   // @Column(name = "userid", nullable = false)
    private int userId;

    public long getTotalPrice() {
        long totalPrice = 0;
        for (OrderItem item : orderItems) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }
}
