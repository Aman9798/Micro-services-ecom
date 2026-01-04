package com.example.Product.Mapper;

import com.example.Product.DTO.*;
import com.example.Product.Entity.Product;
import com.example.Product.Entity.Review;
import com.example.Product.Enums.Brand;
import com.example.Product.Enums.Category;
import com.example.Product.Enums.Gender;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductCardResponseDTO convertToProductCardResponseDTO(Product product, List<ReviewResponseDTO> reviews) {
        return ProductCardResponseDTO.builder()
                .name(product.getName())
                .price(product.getPrice())
                .imageURL(product.getImageURL())
                .category(product.getCategory().toString())
                .reviews(reviews)
                .build();
    }

    public static Product convertToProduct(ProductRequestDTO productRequest) {
        return Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .category(Category.isValidCategory(productRequest.getCategory()))
                .brand(Brand.isValidBrand(productRequest.getBrand()))
                .gender(Gender.isValidGender(productRequest.getGender()))
                .imageURL(productRequest.getImageURL())
                .build();
    }

    public static ProductResponseDTO convertToProductResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .averageRating(product.getAverageRating())
                .reviewCount(product.getReviewCount())
                .brand(product.getBrand())
                .gender(product.getGender())
                .imageURL(product.getImageURL())
                .build();
    }
}
