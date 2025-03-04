package com.example.Cart.DTO;

import jakarta.validation.constraints.Min;

public class UpdateProductDTO {

    @Min(value = 0, message = "product Quantity cannot be  negative")
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
