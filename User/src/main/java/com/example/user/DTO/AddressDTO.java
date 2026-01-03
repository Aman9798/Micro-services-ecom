package com.example.user.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressDTO {
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
