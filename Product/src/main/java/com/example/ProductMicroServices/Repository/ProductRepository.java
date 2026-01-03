package com.example.ProductMicroServices.Repository;

import com.example.ProductMicroServices.Enums.Category;
import com.example.ProductMicroServices.Entity.Product;
import com.example.ProductMicroServices.Enums.Gender;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, Integer> {
    List<Product> findByCategory(Category category);

    List<Product> findByGender(Gender productGender);
}

