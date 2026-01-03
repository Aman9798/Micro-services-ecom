package com.example.Cart.DTO;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {

    @NotNull(message = "ProductId cannot be null")
    private int productId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1,message = "Quantity cannot be 0 or negative")
    private int quantity;

    private int addressId;
}
