package com.example.ProductMicroServices.DTO;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterProductsDTO {

    @Min(value = 0,message = "minimum price should be at least 0")
    private Long minPrice;

    @Min(value = 0,message = "minimum price should be at least 0")
    private Long maxPrice;

    private  String brand;

    private String searchTerm;

    private String sortBy;

    private String sortDirection;

    private String gender;

    private String category;
}
