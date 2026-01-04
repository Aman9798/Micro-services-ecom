package com.example.user.DTO;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddressResponseDTO {
    @Id
    private Integer id;

    @NotNull(message = "Street can't be empty")
    @Size(min = 1, max= 100,message ="Street can't be empty")
    private String street;

    @NotNull(message = "City can't be empty")
    @Size(min = 1, max= 100,message ="City can't be empty")
    private String city;

    @NotNull(message = "State can't be empty")
    @Size(min = 1, max= 100,message ="State can't be empty")
    private String state;

    @NotNull(message = "ZipCode can't be empty")
    @Size(min = 1, max= 100,message ="ZipCode can't be empty")
    private String zipCode;
}
