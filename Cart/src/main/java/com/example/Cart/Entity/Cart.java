package com.example.Cart.Entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartId;

    @OneToMany(mappedBy = "cart",orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CartItem> cartItems = new ArrayList<>();

    @Column(unique = true)
    private int userID;

    public long getTotalPrice() {
        long totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }
}
