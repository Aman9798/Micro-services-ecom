package com.example.ProductMicroServices.Mapper;

import com.example.ProductMicroServices.DTO.ProductCardDTO;
import com.example.ProductMicroServices.DTO.ReviewDTO;
import com.example.ProductMicroServices.Entity.Product;

import java.util.List;

public class ProductMapper {

    public static ProductCardDTO convertToProductCardDTO(Product product, List<ReviewDTO> reviews) {
        return ProductCardDTO.builder()
                .name(product.getName())
                .price(product.getPrice())
                .imageURL(product.getImageURL())
                .category(product.getCategory().toString())
                .reviews(reviews)
                .build();
    }
}
