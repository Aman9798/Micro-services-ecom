package com.example.Cart.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Orders order;

    public int getOrderItemId() {
        return orderItemId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }


    public Long getPrice() {
        return price*quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }
}
