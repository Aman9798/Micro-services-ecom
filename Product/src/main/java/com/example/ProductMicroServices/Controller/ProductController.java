package com.example.ProductMicroServices.Controller;


import com.example.ProductMicroServices.DTO.FilterProductsDTO;
import com.example.ProductMicroServices.DTO.ProductDTO;
import com.example.ProductMicroServices.DTO.UpdateProductDTO;
import com.example.ProductMicroServices.Enums.Brand;
import com.example.ProductMicroServices.Entity.Product;
import com.example.ProductMicroServices.Services.ProductServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductServices productServices;

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = productServices.getAllProducts();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @GetMapping("/{prodId}")
    public ResponseEntity<Product> getProductByID(@PathVariable Integer prodId){
        Product product = productServices.getProductById(prodId);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    //get products
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {

        List<Product> products = productServices.getProductsByCategory(category);
        return new ResponseEntity<>(products,HttpStatus.OK);

    }

    //create a product
    @PostMapping("/")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductDTO newProduct,@RequestHeader("Authorization") String authHeader){

        String token = authHeader.substring(7);
        Product product = productServices.addProduct(newProduct,token);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }

    //update a product
    @PatchMapping("/{prodId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer prodId, @Valid @RequestBody UpdateProductDTO productDTO, @RequestHeader("Authorization") String authHeader){

        String token = authHeader.substring(7);
        Product product = productServices.updateProduct(prodId,productDTO,token);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    //delete a product
    @DeleteMapping("/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer prodId,@RequestHeader("Authorization") String authHeader){

        String token = authHeader.substring(7);
        productServices.deleteProduct(prodId,token);
        return new ResponseEntity<>("Product Deleted",HttpStatus.NO_CONTENT);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(@RequestBody FilterProductsDTO filters) {

        List<Product> products = productServices.filterProducts(filters);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
