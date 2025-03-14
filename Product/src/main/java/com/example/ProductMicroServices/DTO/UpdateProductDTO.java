package com.example.ProductMicroServices.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateProductDTO {

    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private String name;



    @Size(min=2 , max = 500, message = "Description cannot exceed 500 characters")
    private String description;


    @Min(value = 0,message = "Price cannot be negative")
    private Long price;



    @Min(value = 0,message = "Stock cannot be negative")
    private Long stock;

    private String imageURL;


    private String category;


    private String brand;

    private String gender;

    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public Long getPrice() {
        return price;
    }


    public Long getStock() {
        return stock;
    }


    public String getCategory(){
        return category;
    }


    public String getBrand() {
        return brand;
    }


    public String getImageURL(){
        return imageURL;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
