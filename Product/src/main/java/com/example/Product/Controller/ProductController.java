package com.example.Product.Controller;


import com.example.Product.DTO.*;
import com.example.Product.Entity.Product;
import com.example.Product.Service.ProductService;
import com.example.Product.Utils.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@CrossOrigin
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping("/")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts(){
        List<ProductResponseDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductByID(@PathVariable Integer productId){
        ProductResponseDTO product = productService.getProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/card")
    public ResponseEntity <List<ProductCardResponseDTO>> getAllProductCards(){
        List<ProductCardResponseDTO> productCards = productService.getAllProductCardsDetails();
        return new ResponseEntity<>(productCards, HttpStatus.OK);
    }

    //get products
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable String category) {

        List<ProductResponseDTO> products = productService.getProductsByCategory(category);
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByGender(@PathVariable String gender) {

        List<ProductResponseDTO> products = productService.getProductsByGender(gender);
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    //create a product
    @PostMapping("/")
    public ResponseEntity<ProductResponseDTO> addProduct(@Valid @RequestBody ProductRequestDTO productRequest, @RequestHeader("Authorization") String authHeader){

        String token = jwtTokenUtil.getToken(authHeader);
        ProductResponseDTO product = productService.addProduct(productRequest, token);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    //update a product
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Integer productId, @Valid @RequestBody UpdateProductRequestDTO updateProductRequest, @RequestHeader("Authorization") String authHeader){

        String token = jwtTokenUtil.getToken(authHeader);
        ProductResponseDTO product = productService.updateProduct(productId, updateProductRequest, token);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    //delete a product
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer productId, @RequestHeader("Authorization") String authHeader){
        String token = jwtTokenUtil.getToken(authHeader);
        productService.deleteProduct(productId, token);
        return new ResponseEntity<>("Product Deleted", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<ProductResponseDTO>> filterProducts(@RequestBody FilterProductsDTO filters) {
        List<ProductResponseDTO> products = productService.filterProducts(filters);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PatchMapping("/reduceStock/{productId}")
    public void reduceStock(@RequestParam int quantity, @PathVariable Integer productId){

        productService.reduceStock(productId, quantity);
    }

}
