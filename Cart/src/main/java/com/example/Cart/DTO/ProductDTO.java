package com.example.Cart.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ProductDTO {

    private int id;

    @Size(min = 2, max = 255 , message = "Length of name should be between 2 and 500")
    private String name;

    @Size(min = 2, max = 500 , message = "Length of description should be between 2 and 500")
    private String description;

    private Long price;

    private Long stock;

    private String category;

    private String brand;

    private String imageURL;

    private String gender;
}