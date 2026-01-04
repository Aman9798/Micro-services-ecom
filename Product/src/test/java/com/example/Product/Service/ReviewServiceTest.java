package com.example.Product.Service;

import com.example.Product.DTO.ReviewRequestDTO;
import com.example.Product.DTO.ReviewResponseDTO;
import com.example.Product.Delegate.OrderDelegate;
import com.example.Product.Entity.Product;
import com.example.Product.Entity.Review;
import com.example.Product.Exception.ProductNotFoundException;
import com.example.Product.Mapper.ProductMapper;
import com.example.Product.Mapper.ReviewMapper;
import com.example.Product.Repository.ProductRepository;
import com.example.Product.Repository.ReviewRepository;
import com.example.Product.Utils.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderDelegate orderDelegate;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    private final Integer PRODUCT_ID = 123;
    private final String REVIEW_ID = "345";
    private final String AUTH_HEADER = "auth_header";
    private final String VALID_TOKEN = "valid_token";
    private final String INVALID_TOKEN = "invalid_token";
    private final String USER_ID = "user_id";

    @Test
    void createReview_success() {
        ReviewRequestDTO requestDTO = ReviewRequestDTO.builder()
                .productId(PRODUCT_ID)
                .userName("")
                .rating(4)
                .comment("")
                .build();

        Review review = ReviewMapper.convertToReview(requestDTO, USER_ID);

        Product product = Product.builder()
                .id(PRODUCT_ID)
                .averageRating(4.0)
                .reviewCount(1)
                .build();

        when(jwtTokenUtil.getToken(AUTH_HEADER))
                .thenReturn(VALID_TOKEN);
        when(orderDelegate.hasUserBoughtProduct(PRODUCT_ID, AUTH_HEADER))
                .thenReturn(true);
        when(jwtTokenUtil.getUserId(VALID_TOKEN))
                .thenReturn(USER_ID);
        when(reviewRepository.save(any(Review.class)))
                .thenReturn(review);
        when(reviewRepository.findByProductId(PRODUCT_ID))
                .thenReturn(List.of(review));
        when(productRepository.findById(PRODUCT_ID)).
                thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class)))
                .thenReturn(product);

        ReviewResponseDTO result =
                reviewService.createReview(requestDTO, AUTH_HEADER);

        assertNotNull(result);
        verify(reviewRepository).save(any(Review.class));
        verify(productRepository).save(product);
    }

    @Test
    void addReview_productNotFound() {
        ReviewRequestDTO requestDTO = ReviewRequestDTO.builder()
                .productId(PRODUCT_ID)
                .userName("")
                .rating(4)
                .comment("")
                .build();

        Review review = ReviewMapper.convertToReview(requestDTO, USER_ID);

        when(jwtTokenUtil.getToken(AUTH_HEADER))
                .thenReturn(VALID_TOKEN);
        when(orderDelegate.hasUserBoughtProduct(PRODUCT_ID, AUTH_HEADER))
                .thenReturn(true);
        when(jwtTokenUtil.getUserId(VALID_TOKEN))
                .thenReturn(USER_ID);
        when(reviewRepository.save(any(Review.class)))
                .thenReturn(review);
        when(reviewRepository.findByProductId(PRODUCT_ID))
                .thenReturn(List.of(review));
        when(productRepository.findById(PRODUCT_ID))
                .thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> reviewService.createReview(requestDTO, AUTH_HEADER)
        );
    }

    @Test
    void getReviewsByProductId_success() {
        List<Review> reviews = List.of(
                Review.builder().build(),
                Review.builder().build()
        );

        ReviewResponseDTO reviewResponse = ReviewResponseDTO.builder().build();

        when(reviewRepository.findByProductId(PRODUCT_ID))
                .thenReturn(reviews);

        try (MockedStatic<ReviewMapper> mockedStatic =
                     mockStatic(ReviewMapper.class)) {
            mockedStatic
                    .when(() -> ReviewMapper.convertToReviewResponseDTO(any(Review.class)))
                    .thenReturn(reviewResponse);

            List<ReviewResponseDTO> result =
                    reviewService.getReviewsByProductId(PRODUCT_ID);

            assertEquals(2, result.size());
        }
    }

    @Test
    void getReviewsByProductId_empty() {
        when(reviewRepository.findByProductId(PRODUCT_ID))
                .thenReturn(Collections.emptyList());

        List<ReviewResponseDTO> result =
                reviewService.getReviewsByProductId(PRODUCT_ID);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteReview_success() {
        Review review = new Review();

        when(reviewRepository.findById(REVIEW_ID))
                .thenReturn(Optional.of(review));

        boolean result = reviewService.deleteReview(REVIEW_ID);

        verify(reviewRepository).deleteById(REVIEW_ID);
        assertTrue(result);
    }

    @Test
    void deleteReview_failure() {
        Review review = new Review();

        when(reviewRepository.findById(REVIEW_ID))
                .thenReturn(Optional.empty());

        boolean result = reviewService.deleteReview(REVIEW_ID);

        assertFalse(result);
    }
}
