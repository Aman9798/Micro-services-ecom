package com.example.Product.Service;

import com.example.Product.DTO.*;
import com.example.Product.Enums.Gender;
import com.example.Product.Exception.InsufficientStockException;
import com.example.Product.Filter.ProductQueryService;
import com.example.Product.Mapper.ProductMapper;
import com.example.Product.Mapper.ReviewMapper;
import com.example.Product.Utils.JwtTokenUtil;
import com.example.Product.Enums.Brand;
import com.example.Product.Enums.Category;
import com.example.Product.Exception.ProductNotFoundException;
import com.example.Product.Exception.UnauthorizedAccess;
import com.example.Product.Entity.Product;
import com.example.Product.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ProductQueryService productQueryService;

    private static final Logger logger = Logger.getLogger(ProductService.class.getName());

    public List<Product> getAllProducts() {
        try {
            logger.info("Fetching all products");
            return productRepository.findAll();
        } catch (Exception e) {
            logger.severe("Error fetching all products: " + e.getMessage());
            throw e;
        }
    }

    public Product getProductById(Integer prodId) {
        try {
            logger.info("Fetching product with ID: {}"+ prodId);
            return productRepository.findById(prodId).orElseThrow(() -> new ProductNotFoundException("No such Product Present"));
        } catch (Exception e) {
            logger.severe("Error fetching the product with prod ID " + prodId);
            throw e;
        }
    }

    public List<Product> getProductsByCategory(String category) {
        try {
            logger.info("Fetching products by category: {}"+ category);
            Category productCategory = Category.isValidCategory(category);
            return productRepository.findByCategory(productCategory);
        } catch (Exception e) {
            logger.severe("Error fetching the product with category : " + category);
            throw e;
        }
    }

    public List<Product> getProductsByGender(String gender) {
        try {
            logger.info("Fetching products by category: {}"+ gender);
            Gender productGender = Gender.isValidGender(gender);
            return productRepository.findByGender(productGender);
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

            Product newProduct = ProductMapper.convertToProduct(product);
            newProduct.setId();

            productRepository.save(newProduct);
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

            Product product = productRepository.findById(prodId).orElseThrow(() -> new ProductNotFoundException("No such Product Present"));

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
                Category productCategory = Category.isValidCategory(productDTO.getCategory());
                product.setCategory(productCategory);
            }
            if (productDTO.getBrand() != null && !productDTO.getBrand().isEmpty()) {
                Brand productBrand = Brand.isValidBrand(productDTO.getBrand());
                product.setBrand(productBrand);
            }

            if(productDTO.getGender() != null && !productDTO.getGender().isEmpty()){
                Gender productGender = Gender.isValidGender(productDTO.getGender());
                product.setGender(productGender);
            }

            productRepository.save(product);
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
            productRepository.findById(prodId).orElseThrow(() -> new ProductNotFoundException("No such Product Present"));
            productRepository.deleteById(prodId);
            logger.info("Product deleted successfully: {}" + prodId);
        } catch (Exception e) {
            logger.severe("Error deleting the product with prod ID " + prodId);
            throw e;
        }
    }

    public List<Product> filterProducts(FilterProductsDTO filters) {
        try {
            return productQueryService.filterProducts(filters);
        } catch (Exception e) {
            logger.severe("Error fetching the products: " + e.getMessage());
            throw e;
        }
    }

    public void reduceStock(Integer prodId, int quantity) {
        try {
            Product product = productRepository.findById(prodId).orElseThrow(() -> new ProductNotFoundException("No such Product Present"));

            if (quantity > product.getStock()) {
                throw new InsufficientStockException("Insufficient stock");
            }

            product.setStock(product.getStock() - quantity);
            productRepository.save(product);
        } catch (Exception e) {
            logger.severe("Error reducing the product stock") ;
            throw e;
        }
    }

    public List<ProductCardDTO> getAllProductCardsDetails() {
        try {
            List<Product> products = productRepository.findAll();
            return products.stream().map(product -> {
                List<ReviewDTO> productReviews = reviewService.getReviewsByProductId(product.getId()).stream()
                        .map(ReviewMapper::convertToReviewDTO)
                        .collect(Collectors.toList());

                return ProductMapper.convertToProductCardDTO(product, productReviews);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error getting all product cards details: " + e.getMessage());
            throw new RuntimeException("Error getting all product cards details", e);
        }
    }
}