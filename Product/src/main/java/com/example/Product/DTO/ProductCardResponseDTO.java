package com.example.Product.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCardResponseDTO {
    @NotNull(message = "Name can't be null")
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private String name;

    @NotNull(message = "Price can't be null")
    @Min(value = 0,message = "Price cannot be negative")
    private Long price;

    private String imageURL;

    @NotNull(message = "Category can't be null")
    private String category;

    List<ReviewResponseDTO> reviews;
}
