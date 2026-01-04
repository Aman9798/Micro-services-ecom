package com.example.Product.Mapper;

import com.example.Product.DTO.ReviewRequestDTO;
import com.example.Product.DTO.ReviewResponseDTO;
import com.example.Product.Entity.Review;

import java.util.Date;

public class ReviewMapper {

    public static ReviewRequestDTO convertToReviewDTO(Review review) {
        return ReviewRequestDTO.builder()
                .productId(review.getProductId())
                .userName(review.getUserName())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }

    public static Review convertToReview(ReviewRequestDTO reviewRequestDTO, String userId) {
        return Review.builder()
                .productId(reviewRequestDTO.getProductId())
                .userId(userId)
                .userName(reviewRequestDTO.getUserName())
                .rating(reviewRequestDTO.getRating())
                .comment(reviewRequestDTO.getComment())
                .createdAt(new Date())
                .build();
    }

    public static ReviewResponseDTO convertToReviewResponseDTO(Review review) {
        return ReviewResponseDTO.builder()
                .productId(review.getProductId())
                .userName(review.getUserName())
                .rating(review.getRating())
                .comment(review.getComment())
                .build();
    }
}
