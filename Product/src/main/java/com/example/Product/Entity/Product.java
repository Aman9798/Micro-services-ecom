package com.example.Product.Entity;

import com.example.Product.Enums.Brand;
import com.example.Product.Enums.Category;

import com.example.Product.Enums.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.*;

import jakarta.persistence.*;

import java.util.Random;

@Document(collection = "products")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 2, max = 255 , message = "Length of name should be between 2 and 255")
    @Indexed
    private String name;

    @Size(min = 2, max = 500 , message = "Length of description should be between 2 and 500")
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