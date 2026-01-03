package com.example.Cart.DTO;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    @NotNull(message = "Street can't be empty")
    private String street;

    private Integer id;

    @NotNull(message = "City can't be empty")
    private String city;

    @NotNull(message = "State can't be empty")
    private String state;

    @NotNull(message = "ZipCode can't be empty")
    private String zipCode;

    @Override
    public String toString() {
        return street + ", " +
                city + ", " +
                state + ", " +
                zipCode;
    }


}
