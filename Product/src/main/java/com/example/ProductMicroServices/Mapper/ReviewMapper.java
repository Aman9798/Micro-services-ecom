package com.example.ProductMicroServices.Mapper;

import com.example.ProductMicroServices.DTO.ReviewDTO;
import com.example.ProductMicroServices.Entity.Review;

public class ReviewMapper {

    public static ReviewDTO convertToReviewDTO(Review review) {
        return ReviewDTO.builder()
                .productId(review.getProductId())
                .userName(review.getUserName())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }
}
