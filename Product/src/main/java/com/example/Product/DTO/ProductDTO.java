package com.example.Product.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @NotNull(message = "Gender can't be null")
    private String gender;

    @NotNull(message = "Category can't be null")
    private String category;

    @NotNull(message = "Brand can't be null")
    private String brand;
}
