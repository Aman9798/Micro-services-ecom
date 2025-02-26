package com.example.ProductMicroServices.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;



@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {


    @NotNull(message = "Name can't be null")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private String name;


    @NotNull(message = "Description can't be null")
    @Size(min=2 , max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Price can't be null")
    @Min(value = 0,message = "Price cannot be negative")
    private Long price;


    @NotNull(message = "Stock can't be null")
    @Min(value = 0,message = "Stock cannot be negative")
    private Long stock;

    private String imageURL;

    public ProductDTO(String name, String description, Long price, Long stock, String imageURL, String category, String brand) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageURL = imageURL;
        this.category = category;
        this.brand = brand;
    }

    @NotNull(message = "Category can't be null")
    private String category;

    @NotNull(message = "Brand can't be null")
    private String brand;

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


}
