package com.example.Product.Service;

import com.example.Product.DTO.ReviewResponseDTO;
import com.example.Product.Delegate.OrderDelegate;
import com.example.Product.DTO.ReviewRequestDTO;
import com.example.Product.Entity.Product;
import com.example.Product.Entity.Review;
import com.example.Product.Exception.ProductNotFoundException;
import com.example.Product.Mapper.ReviewMapper;
import com.example.Product.Repository.ProductRepository;
import com.example.Product.Repository.ReviewRepository;
import com.example.Product.Utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private OrderDelegate orderDelegate;

    @Autowired
    private ProductRepository productRepository;

    public ReviewResponseDTO createReview(ReviewRequestDTO review, String authHeader) {

        String token = jwtTokenUtil.getToken(authHeader);
        if(!orderDelegate.hasUserBoughtProduct(review.getProductId(), authHeader)){
            throw new RuntimeException("Cannot add review");
        }

        Review newReview = ReviewMapper.convertToReview(review, jwtTokenUtil.getUserId(token));

        reviewRepository.save(newReview);
        updateProductReviewData(review.getProductId());

        return ReviewMapper.convertToReviewResponseDTO(newReview);
    }

    private void updateProductReviewData(int productId) {

        List<Review> allReviews = reviewRepository.findByProductId(productId);

        Double avgRating = 0.0;
        if (!allReviews.isEmpty()) {
            double sum = 0;
            for (Review review : allReviews) {
                sum += review.getRating();
            }
            avgRating = sum / allReviews.size();
        }

        int reviewCount = allReviews.size();

        Product product = productRepository
                .findById(productId)
                .orElseThrow(
                        () -> new ProductNotFoundException("No such Product Present")
                );
        product.setAverageRating(avgRating);
        product.setReviewCount(reviewCount);

        productRepository.save(product);
    }

    public List<ReviewResponseDTO> getReviewsByProductId(int productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        return reviews.stream()
                .map(ReviewMapper::convertToReviewResponseDTO)
                .toList();
    }

    public boolean deleteReview(String id) {
        Optional<Review> review = reviewRepository.findById(id);

        if (review.isPresent()) {
            reviewRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Double getAverageRatingForProduct(int productId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("productId").is(productId)),
                Aggregation.group().avg("rating").as("averageRating")
        );

        AggregationResults<Map> result = mongoTemplate.aggregate(
                aggregation, "reviews", Map.class
        );

        Map resultMap = result.getUniqueMappedResult();

        if (resultMap != null && resultMap.get("averageRating") != null) {
            return (Double) resultMap.get("averageRating");
        }

        return 0.0;
    }

    public long getReviewCountForProduct(int productId) {
        return reviewRepository.countByProductId(productId);
    }

    public List<Review> getReviewsByProductIdAndRating(int productId, Integer rating) {
        return reviewRepository.findByProductIdAndRating(productId, rating);
    }
}