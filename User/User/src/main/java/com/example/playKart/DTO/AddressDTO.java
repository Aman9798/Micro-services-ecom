package com.example.playKart.DTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddressDTO {
    @NotNull(message = "Street can't be empty")
    @Size(min = 1, max= 100,message ="Street can't be empty")
    private String street;



    public AddressDTO(String street, String city, String state, String zipCode) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    @NotNull(message = "City can't be empty")
    @Size(min = 1, max= 100,message ="City can't be empty")
    private String city;

    @NotNull(message = "State can't be empty")
    @Size(min = 1, max= 100,message ="State can't be empty")
    private String state;

    @NotNull(message = "ZipCode can't be empty")
    @Size(min = 1, max= 100,message ="ZipCode can't be empty")
    private String zipCode;

    public String getStreet(){
        return street;
    }

    public String getCity(){
        return city;
    }

    public String getState(){
        return state;
    }

    public String getZipCode(){
        return zipCode;
    }
}
