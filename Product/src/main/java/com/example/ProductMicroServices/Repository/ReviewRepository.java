package com.example.ProductMicroServices.Repository;

import com.example.ProductMicroServices.Entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {


    List<Review> findByProductId(int productId);


    List<Review> findByProductIdAndRating(int productId, Integer rating);

    long countByProductId(int productId);

    void deleteByProductId(int productId);
}