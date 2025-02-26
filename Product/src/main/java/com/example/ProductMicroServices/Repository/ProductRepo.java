package com.example.ProductMicroServices.Repository;

import com.example.ProductMicroServices.Enums.Brand;
import com.example.ProductMicroServices.Enums.Category;
import com.example.ProductMicroServices.Entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepo extends MongoRepository<Product, Integer> {
    List<Product> findByCategory(Category category);
    @Query("{ 'price': { '$gt': ?0, '$lt': ?1 }, '$or': [ { 'brand': ?2 }, { 'brand': { '$exists': true } } ], '$or': [ { 'name': { '$regex': ?3, '$options': 'i' } }, { 'description': { '$regex': ?3, '$options': 'i' } } ] }")
    List<Product> filterProducts(Long minPrice, Long maxPrice, Brand brand, String searchTerm, Sort sort);


}

