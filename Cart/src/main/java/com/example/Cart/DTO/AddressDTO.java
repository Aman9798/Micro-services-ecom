package com.example.Cart.DTO;
import jakarta.validation.constraints.NotNull;

public class AddressDTO {
    @NotNull(message = "Street can't be empty")
    private String street;

    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AddressDTO(int id, String street, String city, String state, String zipCode) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    @NotNull(message = "City can't be empty")
    private String city;

    @NotNull(message = "State can't be empty")
    private String state;

    @NotNull(message = "ZipCode can't be empty")
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

    @Override
    public String toString() {
        return street + ", " +
                city + ", " +
                state + ", " +
                zipCode;
    }


}
