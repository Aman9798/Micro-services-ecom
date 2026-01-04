package com.example.Product.DTO;

import com.example.Product.Enums.Brand;
import com.example.Product.Enums.Category;
import com.example.Product.Enums.Gender;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDTO {

    private int id;

    @Size(min = 2, max = 255 , message = "name cannot be more than 100 characters")
    private String name;

    @Size(min = 2, max = 500 , message = "Length of description should be between 2 and 500")
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
}
