package com.example.ProductMicroServices.Services;

import com.example.ProductMicroServices.DTO.ProductDTO;
import com.example.ProductMicroServices.DTO.ReviewDTO;
import com.example.ProductMicroServices.Entity.Product;
import com.example.ProductMicroServices.Entity.Review;
import com.example.ProductMicroServices.Repository.ProductRepo;
import com.example.ProductMicroServices.Repository.ReviewRepository;
import com.example.ProductMicroServices.Services.ProductServices;
import com.example.ProductMicroServices.Utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Date;
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
    private WebClient webClient;

    @Autowired
    private ProductRepo productRepository;

    public ReviewDTO createReview(ReviewDTO review,String authHeader) {

        String token = authHeader.substring(7);
        if(!hasUserBoughtProduct(review.getProductId(),authHeader)){
            throw new RuntimeException("Cannot add review");
        }

        System.out.println("order was created by user");
        Review newReview = new Review();
        String userId = jwtTokenUtil.getUserId(token);
        newReview.setProductId(review.getProductId());
        newReview.setUserId(userId);
        newReview.setCreatedAt(new Date());
        newReview.setRating(review.getRating());
        newReview.setComment(review.getComment());
        newReview.setUserName(review.getUserName());
        reviewRepository.save(newReview);

        updateProductReviewData(review.getProductId());




        return review;
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


        Product product = productRepository.findById(productId).orElseThrow();
        product.setAverageRating(avgRating);
        product.setReviewCount(reviewCount);

        productRepository.save(product);
    }

    private boolean hasUserBoughtProduct(int productId, String authHeader) {
        String cartServiceUrl = "http://localhost:8082/order/product/" + productId;
        try {

            return Boolean.TRUE.equals(webClient.get()
                    .uri(cartServiceUrl)
                    .header("Authorization", authHeader)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("No such order found")))
                    .bodyToMono(Boolean.class) // Expect a boolean response
                    .defaultIfEmpty(false)
                    .onErrorReturn(false)
                    .block());
        } catch (Exception e) {
            throw new RuntimeException("Error checking product purchase status", e);
        }
    }


    public List<Review> getReviewsByProductId(int productId) {
        return reviewRepository.findByProductId(productId);
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