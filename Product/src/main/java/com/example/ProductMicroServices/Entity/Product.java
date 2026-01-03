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
@Builder
@Getter
@Setter
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

    @Enumerated
    private Brand brand;

    @Enumerated
    private Gender gender;

    private String imageURL;

    public void setId() {
        Random random = new Random();
        this.id = random.nextInt(1000000);
    }
}