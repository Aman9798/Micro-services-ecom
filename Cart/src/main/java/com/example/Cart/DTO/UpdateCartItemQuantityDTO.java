package com.example.Cart.DTO;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemQuantityDTO {

    @Min(value = 0, message = "product Quantity cannot be  negative")
    private int quantity;
}
