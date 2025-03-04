package com.example.ProductMicroServices.Services;

import com.example.ProductMicroServices.DTO.FilterProductsDTO;
import com.example.ProductMicroServices.DTO.UpdateProductDTO;
import com.example.ProductMicroServices.Utils.JwtTokenUtil;
import com.example.ProductMicroServices.DTO.ProductDTO;
import com.example.ProductMicroServices.Enums.Brand;
import com.example.ProductMicroServices.Enums.Category;
import com.example.ProductMicroServices.Exception.ProductNotFoundException;
import com.example.ProductMicroServices.Exception.UnauthorizedAccess;
import com.example.ProductMicroServices.Entity.Product;
import com.example.ProductMicroServices.Repository.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServices {

    @Autowired
    ProductRepo productRepo;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(ProductServices.class);

    public List<Product> getAllProducts() {
        try {
            logger.info("Fetching all products");
            return productRepo.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all products", e);
        }
    }

    public Product getProductById(Integer prodId) {
        try {
            logger.info("Fetching product with ID: {}", prodId);
            return productRepo.findById(prodId).orElseThrow(() -> new ProductNotFoundException("No such Product Present"));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching product by ID", e);
        }
    }

    public List<Product> getProductsByCategory(String category) {
        try {
            logger.info("Fetching products by category: {}", category);
            Category productCategory = Category.SportsEquipments.isValidCategory(category);
            return productRepo.findByCategory(productCategory);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching products by category", e);
        }
    }

    public Product addProduct(ProductDTO product, String token) {
        try {
            logger.info("Adding new product: {}", product.getName());
            if (!jwtTokenUtil.isAdmin(token)) {
                logger.warn("Unauthorized access attempt to add product");
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
            productRepo.save(newProduct);
            logger.info("Product added successfully: {}", newProduct.getId());
            return newProduct;
        } catch (Exception e) {
            throw new RuntimeException("Error adding product", e);
        }
    }

    public Product updateProduct(Integer prodId, UpdateProductDTO productDTO, String token) {
        try {
            logger.info("Updating product with ID: {}", prodId);
            if (!jwtTokenUtil.isAdmin(token)) {
                logger.warn("Unauthorized access attempt to update product");
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
                product.setImageURL(product.getImageURL());
            }
            if (productDTO.getCategory() != null && !productDTO.getCategory().isEmpty()) {
                Category productCategory = Category.SportsEquipments.isValidCategory(productDTO.getCategory());
                product.setCategory(productCategory);
            }
            if (productDTO.getBrand() != null && !productDTO.getBrand().isEmpty()) {
                Brand productBrand = Brand.Adidas.isValidBrand(productDTO.getBrand());
                product.setBrand(productBrand);
            }

            productRepo.save(product);
            logger.info("Product updated successfully: {}", product.getId());
            return product;
        } catch (Exception e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    public void deleteProduct(Integer prodId, String token) {
        try {
            logger.info("Deleting product with ID: {}", prodId);
            if (!jwtTokenUtil.isAdmin(token)) {
                logger.warn("Unauthorized access attempt to delete product");
                throw new UnauthorizedAccess("This is an admin functionality");
            }
            productRepo.findById(prodId).orElseThrow(() -> new ProductNotFoundException("No such Product Present"));
            productRepo.deleteById(prodId);
            logger.info("Product deleted successfully: {}", prodId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    public List<Product> filterProducts(FilterProductsDTO filters) {
        try {
            Long minPrice = filters.getMinPrice() == null ? 0 : filters.getMinPrice();
            Long maxPrice = filters.getMaxPrice() == null ? Integer.MAX_VALUE : filters.getMaxPrice();
            Brand productBrand = null;
            if (filters.getBrand() != null) {
                productBrand = Brand.Adidas.isValidBrand(filters.getBrand());
            }
            String searchTerm = "";
            if (filters.getSearchTerm() != null) {
                searchTerm = filters.getSearchTerm();
            }
            Sort sort = Sort.by(Sort.Order.asc("price"));
            if (filters.getSortDirection() == null) {
                filters.setSortDirection("asc");
            }
            if (filters.getSortBy() != null) {
                if (!filters.getSortBy().equals("price") && !filters.getSortBy().equals("name")) {
                    throw new RuntimeException("Not a valid sorting field");
                }
                if (!filters.getSortDirection().equals("asc") && !filters.getSortDirection().equals("desc")) {
                    throw new RuntimeException("Not a valid sorting parameter");
                }
                Sort.Direction direction = Sort.Direction.fromString(filters.getSortDirection());
                sort = Sort.by(new Sort.Order(direction, filters.getSortBy()).ignoreCase());
            }

            return productRepo.filterProducts(minPrice, maxPrice, productBrand, searchTerm, sort);
        } catch (Exception e) {
            throw new RuntimeException("Error filtering products", e);
        }
    }

    public void reduceStock(Integer prodId, int quantity) {
        try {
            Product product = productRepo.findById(prodId).orElseThrow(() -> new ProductNotFoundException("No such Product Present"));
            product.setStock(product.getStock() - quantity);
            productRepo.save(product);
        } catch (Exception e) {
            throw new RuntimeException("Error reducing stock", e);
        }
    }
}