package com.example.ProductMicroServices.Repository;

import com.example.ProductMicroServices.Enums.Brand;
import com.example.ProductMicroServices.Enums.Category;
import com.example.ProductMicroServices.Entity.Product;
import com.example.ProductMicroServices.Enums.Gender;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepo extends MongoRepository<Product, Integer> {
    List<Product> findByCategory(Category category);
    @Query("{ 'price': { '$gte': ?0, '$lte': ?1 }, '$or': [ { 'name': { '$regex': ?2, '$options': 'i' } }, { 'description': { '$regex': ?2, '$options': 'i' } } ] }")
    List<Product> filterProductsBasic(Long minPrice, Long maxPrice, String searchTerm, Sort sort);

    // Filter by brand
    @Query("{ 'price': { '$gte': ?0, '$lte': ?1 }, 'brand': ?3, '$or': [ { 'name': { '$regex': ?2, '$options': 'i' } }, { 'description': { '$regex': ?2, '$options': 'i' } } ] }")
    List<Product> filterProductsByBrand(Long minPrice, Long maxPrice, String searchTerm, Brand brand, Sort sort);

    // Filter by gender
    @Query("{ 'price': { '$gte': ?0, '$lte': ?1 }, 'gender': ?3, '$or': [ { 'name': { '$regex': ?2, '$options': 'i' } }, { 'description': { '$regex': ?2, '$options': 'i' } } ] }")
    List<Product> filterProductsByGender(Long minPrice, Long maxPrice, String searchTerm, Gender gender, Sort sort);

    // Filter by category
    @Query("{ 'price': { '$gte': ?0, '$lte': ?1 }, 'category': ?3, '$or': [ { 'name': { '$regex': ?2, '$options': 'i' } }, { 'description': { '$regex': ?2, '$options': 'i' } } ] }")
    List<Product> filterProductsByCategory(Long minPrice, Long maxPrice, String searchTerm, Category category, Sort sort);

    // Filter by brand and gender
    @Query("{ 'price': { '$gte': ?0, '$lte': ?1 }, 'brand': ?3, 'gender': ?4, '$or': [ { 'name': { '$regex': ?2, '$options': 'i' } }, { 'description': { '$regex': ?2, '$options': 'i' } } ] }")
    List<Product> filterProductsByBrandAndGender(Long minPrice, Long maxPrice, String searchTerm, Brand brand, Gender gender, Sort sort);

    // Filter by brand and category
    @Query("{ 'price': { '$gte': ?0, '$lte': ?1 }, 'brand': ?3, 'category': ?4, '$or': [ { 'name': { '$regex': ?2, '$options': 'i' } }, { 'description': { '$regex': ?2, '$options': 'i' } } ] }")
    List<Product> filterProductsByBrandAndCategory(Long minPrice, Long maxPrice, String searchTerm, Brand brand, Category category, Sort sort);

    // Filter by gender and category
    @Query("{ 'price': { '$gte': ?0, '$lte': ?1 }, 'gender': ?3, 'category': ?4, '$or': [ { 'name': { '$regex': ?2, '$options': 'i' } }, { 'description': { '$regex': ?2, '$options': 'i' } } ] }")
    List<Product> filterProductsByGenderAndCategory(Long minPrice, Long maxPrice, String searchTerm, Gender gender, Category category, Sort sort);

    // Filter with all criteria
    @Query("{ 'price': { '$gte': ?0, '$lte': ?1 }, 'brand': ?3, 'gender': ?4, 'category': ?5, '$or': [ { 'name': { '$regex': ?2, '$options': 'i' } }, { 'description': { '$regex': ?2, '$options': 'i' } } ] }")
    List<Product> filterProductsWithAllFilters(Long minPrice, Long maxPrice, String searchTerm, Brand brand, Gender gender, Category category, Sort sort);
    List<Product> findByGender(Gender productGender);
}

