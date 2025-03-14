package com.example.Cart.DTO;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;


import java.util.Random;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Size(min = 2, max = 255 , message = "name cannot be more than 100 characters")
    private String name;

    @Size(min = 2, max = 500 , message = "name cannot be more than 1000 characters")
    private String description;

    private Long price;

    private Long stock;

    private String category;

    private String brand;

    private String imageURL;

    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId() {
        Random random = new Random();
        this.id = random.nextInt(1000000);
    }


    public void setName(String name) {
        this.name = name;
    }
    public String getName(){
        return name;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }


    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getPrice(){
        return price;
    }


    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Long getStock(){
        return stock;
    }


    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory(){
        return category;
    }


    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getBrand(){
        return brand;
    }

    public String getImageURL(){
        return imageURL;
    }

    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }
}