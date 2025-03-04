package com.example.Cart.DTO;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CartItemDTO {


    @NotNull(message = "ProductId cannot be null")
    private int productId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1,message = "Quantity cannot be 0 or negative")
    private int quantity;

    private int addressId;

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
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
