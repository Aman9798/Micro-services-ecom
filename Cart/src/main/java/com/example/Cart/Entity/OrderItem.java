package com.example.Cart.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItemId;

    private Date createdAt;

    private int productId;

    private String productName;

    private String productDescription;

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


    public OrderItem( int productId, String productName, String productDescription, Long price, int quantity, int userId, String userName, String address, String phoneNumber,String imageURL) {
        this.createdAt = new Date();
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.price = price;
        this.quantity = quantity;
        this.userId = userId;
        this.userName = userName;
        Address = address;
        this.phoneNumber = phoneNumber;
        this.imageURL = imageURL;
    }

    public OrderItem() {

    }

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

    public String getProductDescription() {
        return productDescription;
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
