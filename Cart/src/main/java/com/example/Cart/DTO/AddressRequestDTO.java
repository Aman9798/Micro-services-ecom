package com.example.Cart.DTO;

import jakarta.validation.constraints.NotNull;

public class AddressRequestDTO {

    @NotNull
    private int addressId;

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
