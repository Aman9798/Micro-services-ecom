package com.example.Product.Mapper;

import com.example.Product.DTO.ProductCardDTO;
import com.example.Product.DTO.ProductDTO;
import com.example.Product.DTO.ReviewDTO;
import com.example.Product.Entity.Product;
import com.example.Product.Enums.Brand;
import com.example.Product.Enums.Category;
import com.example.Product.Enums.Gender;

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

    public static Product convertToProduct(ProductDTO product) {
        return Product.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(Category.isValidCategory(product.getCategory()))
                .brand(Brand.isValidBrand(product.getBrand()))
                .gender(Gender.isValidGender(product.getGender()))
                .imageURL(product.getImageURL())
                .build();
    }
}
