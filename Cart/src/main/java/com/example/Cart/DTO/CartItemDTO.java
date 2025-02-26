package com.example.Cart.DTO;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CartItemDTO {


    @Size(min = 2 , max = 100 , message = "The product name should be at least of 2 characters")
    private String productName;


    @NotNull
    private int productId;

    @NotNull
    private int quantity;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



}
