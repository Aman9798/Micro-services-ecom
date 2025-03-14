package com.example.Cart.Entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
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
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }



}
