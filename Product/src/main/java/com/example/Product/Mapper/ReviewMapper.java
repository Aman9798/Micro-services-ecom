package com.example.Product.Mapper;

import com.example.Product.DTO.ReviewDTO;
import com.example.Product.Entity.Review;

import java.util.Date;

public class ReviewMapper {

    public static ReviewDTO convertToReviewDTO(Review review) {
        return ReviewDTO.builder()
                .productId(review.getProductId())
                .userName(review.getUserName())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }

    public static Review convertToReview(ReviewDTO reviewDTO, String userId) {
        return Review.builder()
                .productId(reviewDTO.getProductId())
                .userId(userId)
                .userName(reviewDTO.getUserName())
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .createdAt(new Date())
                .build();
    }
}
