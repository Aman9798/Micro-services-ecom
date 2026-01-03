package com.example.Cart.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequestDTO {

    @NotNull
    private int addressId;
}
