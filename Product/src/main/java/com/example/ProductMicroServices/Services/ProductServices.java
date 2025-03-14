package com.example.ProductMicroServices.Services;

import com.example.ProductMicroServices.DTO.*;
import com.example.ProductMicroServices.Entity.Review;
import com.example.ProductMicroServices.Enums.Gender;
import com.example.ProductMicroServices.Utils.JwtTokenUtil;
import com.example.ProductMicroServices.Enums.Brand;
import com.example.ProductMicroServices.Enums.Category;
import com.example.ProductMicroServices.Exception.ProductNotFoundException;
import com.example.ProductMicroServices.Exception.UnauthorizedAccess;
import com.example.ProductMicroServices.Entity.Product;
import com.example.ProductMicroServices.Repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServices {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    ReviewService reviewService;

    private static final Logger logger = Logger.getLogger(ProductServices.class.getName());

    public List<Product> getAllProducts() {
        try {
            logger.info("Fetching all products");
            return productRepo.findAll();
        } catch (Exception e) {
            logger.severe("Error fetching all products: " + e.getMessage());
            throw e;

        }
    }

    public Product getProductById(Integer prodId) {
        try {
            logger.info("Fetching product with ID: {}"+ prodId);
            return productRepo.findById(prodId).orElseThrow(() -> new ProductNotFoundException("No such Product Present"));
        } catch (Exception e) {
            logger.severe("Error fetching the product with prod ID " + prodId);
            throw e;
        }
    }

    public List<Product> getProductsByCategory(String category) {
        try {
            logger.info("Fetching products by category: {}"+ category);
            Category productCategory = Category.SportsEquipments.isValidCategory(category);
            return productRepo.findByCategory(productCategory);
        } catch (Exception e) {
            logger.severe("Error fetching the product with category : " + category);
            throw e;
        }
    }

    public List<Product> getProductsByGender(String gender) {
        try {
            logger.info("Fetching products by category: {}"+ gender);
            Gender productGender = Gender.Men.isValidGender(gender);
            return productRepo.findByGender(productGender);
        } catch (Exception e) {
            logger.severe("Error fetching the product with gender " + gender);
            throw e;
        }
    }

    public Product addProduct(ProductDTO product, String token) {
        try {
            logger.info("Adding new product: {}"+ product.getName());
            if (!jwtTokenUtil.isAdmin(token)) {
                logger.warning("Unauthorized access attempt to add product");
                throw new UnauthorizedAccess("This is an admin functionality");
            }

            Product newProduct = new Product();
            newProduct.setId();
            newProduct.setName(product.getName());
            newProduct.setDescription(product.getDescription());
            Category productCategory = Category.SportsEquipments.isValidCategory(product.getCategory());
            newProduct.setCategory(productCategory);
            newProduct.setPrice(product.getPrice());
            newProduct.setStock(product.getStock());
            newProduct.setImageURL(product.getImageURL());
            Brand productBrand = Brand.Adidas.isValidBrand(product.getBrand());
            newProduct.setBrand(productBrand);
            Gender productGender = Gender.Men.isValidGender(product.getGender());
            newProduct.setGender(productGender);

            productRepo.save(newProduct);
            logger.info("Product added successfully: {}"+ newProduct.getId());
            return newProduct;
        } catch (Exception e) {
            logger.severe("Error adding the product ");
            throw e;
        }
    }

    public Product updateProduct(Integer prodId, UpdateProductDTO productDTO, String token) {
        try {
            logger.info("Updating product with ID: {}"+ prodId);
            if (!jwtTokenUtil.isAdmin(token)) {
                logger.warning("Unauthorized access attempt to update product");
                throw new UnauthorizedAccess("This is an admin functionality");
            }

            Product product = productRepo.findById(prodId).orElseThrow(() -> new ProductNotFoundException("No such Product Present"));

            if (productDTO.getName() != null && !productDTO.getName().isEmpty()) {
                product.setName(productDTO.getName());
            }
            if (productDTO.getDescription() != null && !productDTO.getDescription().isEmpty()) {
                product.setDescription(productDTO.getDescription());
            }
            if (productDTO.getPrice() != null) {
                product.setPrice(productDTO.getPrice());
            }
            if (productDTO.getStock() != null) {
                product.setStock(productDTO.getStock());
            }
            if (productDTO.getImageURL() != null && !productDTO.getImageURL().isEmpty()) {
                product.setImageURL(productDTO.getImageURL());
            }
            if (productDTO.getCategory() != null && !productDTO.getCategory().isEmpty()) {
                Category productCategory = Category.SportsEquipments.isValidCategory(productDTO.getCategory());
                product.setCategory(productCategory);
            }
            if (productDTO.getBrand() != null && !productDTO.getBrand().isEmpty()) {
                Brand productBrand = Brand.Adidas.isValidBrand(productDTO.getBrand());
                product.setBrand(productBrand);
            }

            if(productDTO.getGender() != null && !productDTO.getGender().isEmpty()){
                Gender productGender = Gender.Men.isValidGender(productDTO.getGender());
                product.setGender(productGender);
            }

            productRepo.save(product);
            logger.info("Product updated successfully: {}"+ product.getId());
            return product;
        } catch (Exception e) {
            logger.severe("Error updating the product " + prodId);
            throw e;
        }
    }

    public void deleteProduct(Integer prodId, String token) {
        try {
            logger.info("Deleting product with ID: {}" + prodId);
            if (!jwtTokenUtil.isAdmin(token)) {
                logger.warning("Unauthorized access attempt to delete product");
                throw new UnauthorizedAccess("This is an admin functionality");
            }
            productRepo.findById(prodId).orElseThrow(() -> new ProductNotFoundException("No such Product Present"));
            productRepo.deleteById(prodId);
            logger.info("Product deleted successfully: {}" + prodId);
        } catch (Exception e) {
            logger.severe("Error deleting the product with prod ID " + prodId);
            throw e;
        }
    }

    public List<Product> filterProducts(FilterProductsDTO filters) {
        try {
            // Set default values for missing filters
            Long minPrice = filters.getMinPrice() == null ? 0 : filters.getMinPrice();
            Long maxPrice = filters.getMaxPrice() == null ? Integer.MAX_VALUE : filters.getMaxPrice();

            // Handle brand filter
            Brand productBrand = null;
            if (filters.getBrand() != null && !filters.getBrand().isEmpty()) {
                productBrand = Brand.Adidas.isValidBrand(filters.getBrand());
            }

            // Handle search term
            String searchTerm = "";
            if (filters.getSearchTerm() != null) {
                searchTerm = filters.getSearchTerm();
            }

            // Handle sorting - without ignoreCase()
            Sort sort = Sort.by(Sort.Direction.ASC, "price");
            if (filters.getSortDirection() == null || filters.getSortDirection().isEmpty()) {
                filters.setSortDirection("asc");
            }

            if (filters.getSortBy() != null && !filters.getSortBy().isEmpty()) {
                if (!filters.getSortBy().equals("price") && !filters.getSortBy().equals("name")) {
                    throw new RuntimeException("Not a valid sorting field");
                }

                if (!filters.getSortDirection().equals("asc") && !filters.getSortDirection().equals("desc")) {
                    throw new RuntimeException("Not a valid sorting parameter");
                }
                Sort.Direction direction = Sort.Direction.fromString(filters.getSortDirection());
                sort = Sort.by(direction, filters.getSortBy());
            }

            // Handle gender filter
            Gender gender = null;
            if (filters.getGender() != null && !filters.getGender().isEmpty()) {
                gender = Gender.Men.isValidGender(filters.getGender());
            }

            // Handle category filter
            Category productCategory = null;
            if (filters.getCategory() != null && !filters.getCategory().isEmpty()) {
                productCategory = Category.SportsWears.isValidCategory(filters.getCategory());
            }

            // Call the appropriate repository method based on which filters are present
            if (productBrand == null && gender == null && productCategory == null) {
                return productRepo.filterProductsBasic(minPrice, maxPrice, searchTerm, sort);
            } else if (productBrand != null && gender == null && productCategory == null) {
                return productRepo.filterProductsByBrand(minPrice, maxPrice, searchTerm, productBrand, sort);
            } else if (productBrand == null && gender != null && productCategory == null) {
                return productRepo.filterProductsByGender(minPrice, maxPrice, searchTerm, gender, sort);
            } else if (productBrand == null && gender == null && productCategory != null) {
                return productRepo.filterProductsByCategory(minPrice, maxPrice, searchTerm, productCategory, sort);
            } else if (productBrand != null && gender != null && productCategory == null) {
                return productRepo.filterProductsByBrandAndGender(minPrice, maxPrice, searchTerm, productBrand, gender, sort);
            } else if (productBrand != null && gender == null && productCategory != null) {
                return productRepo.filterProductsByBrandAndCategory(minPrice, maxPrice, searchTerm, productBrand, productCategory, sort);
            } else if (productBrand == null && gender != null && productCategory != null) {
                return productRepo.filterProductsByGenderAndCategory(minPrice, maxPrice, searchTerm, gender, productCategory, sort);
            } else {
                return productRepo.filterProductsWithAllFilters(minPrice, maxPrice, searchTerm, productBrand, gender, productCategory, sort);
            }
        } catch (Exception e) {
            logger.severe("Error fetching the products: " + e.getMessage());
            throw e;
        }
    }

    public void reduceStock(Integer prodId, int quantity) {
        try {
            Product product = productRepo.findById(prodId).orElseThrow(() -> new ProductNotFoundException("No such Product Present"));
            product.setStock(product.getStock() - quantity);
            productRepo.save(product);
        } catch (Exception e) {
            logger.severe("Error reducing the product stock") ;
            throw e;
        }
    }

    public List<ProductCardDTO> getAllProductCardsDetails() {
        try {
            List<Product> products = productRepo.findAll();
            return products.stream().map(product -> {
                List<ReviewDTO> productReviews = reviewService.getReviewsByProductId(product.getId()).stream()
                        .map(this::convertToReviewDTO)
                        .collect(Collectors.toList());

                return convertToProductCardDTO(product, productReviews);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error getting all product cards details: " + e.getMessage());
            throw new RuntimeException("Error getting all product cards details", e);
        }
    }

    private ReviewDTO convertToReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setProductId(review.getProductId());
        reviewDTO.setUserName(review.getUserName());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        return reviewDTO;
    }

    private ProductCardDTO convertToProductCardDTO(Product product, List<ReviewDTO> reviews) {
        ProductCardDTO productCardDTO = new ProductCardDTO();
        productCardDTO.setName(product.getName());
        productCardDTO.setPrice(product.getPrice());
        productCardDTO.setImageURL(product.getImageURL());
        productCardDTO.setCategory(product.getCategory().toString());
        productCardDTO.setReviews(reviews);
        return productCardDTO;
    }
}