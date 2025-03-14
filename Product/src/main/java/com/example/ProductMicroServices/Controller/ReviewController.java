package com.example.ProductMicroServices.Controller;

import com.example.ProductMicroServices.DTO.ReviewDTO;
import com.example.ProductMicroServices.Entity.Review;
import com.example.ProductMicroServices.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@CrossOrigin
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO review, @RequestHeader("Authorization") String authHeader) {

        ReviewDTO createdReview = reviewService.createReview(review,authHeader);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }



    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable int productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        boolean deleted = reviewService.deleteReview(id);

        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @GetMapping("/product/{productId}/rating")
    public ResponseEntity<Map<String, Object>> getProductRating(@PathVariable int productId) {
        Double averageRating = reviewService.getAverageRatingForProduct(productId);
        long reviewCount = reviewService.getReviewCountForProduct(productId);

        Map<String, Object> response = new HashMap<>();
        response.put("productId", productId);
        response.put("averageRating", averageRating);
        response.put("reviewCount", reviewCount);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }





}