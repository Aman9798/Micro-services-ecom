package com.example.Product.Repository;

import com.example.Product.Enums.Category;
import com.example.Product.Entity.Product;
import com.example.Product.Enums.Gender;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, Integer> {
    List<Product> findByCategory(Category category);

    List<Product> findByGender(Gender productGender);
}

