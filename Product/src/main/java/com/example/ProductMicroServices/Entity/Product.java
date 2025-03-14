package com.example.ProductMicroServices.Entity;

import com.example.ProductMicroServices.DTO.ReviewDTO;
import com.example.ProductMicroServices.Enums.Brand;
import com.example.ProductMicroServices.Enums.Category;

import com.example.ProductMicroServices.Enums.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;

import jakarta.persistence.*;

import java.util.List;
import java.util.Random;

@Document(collection = "products")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Size(min = 2, max = 255 , message = "name cannot be more than 100 characters")
    @Indexed
    private String name;

    @Size(min = 2, max = 500 , message = "name cannot be more than 1000 characters")
    @Indexed
    private String description;

    private Long price;


    private Long stock;

    @Enumerated
    private Category category;

    private Double averageRating;
    private Integer reviewCount;

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    @Enumerated
    private Brand brand;

    @Enumerated
    private Gender gender;

    private String imageURL;

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


    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory(){
        return category;
    }


    public void setBrand(Brand brand) {
        this.brand = brand;
    }
    public Brand getBrand(){
        return brand;
    }

    public String getImageURL(){
        return imageURL;
    }

    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}