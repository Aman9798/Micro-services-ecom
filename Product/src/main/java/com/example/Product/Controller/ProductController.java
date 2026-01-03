package com.example.Product.Controller;


import com.example.Product.DTO.FilterProductsDTO;
import com.example.Product.DTO.ProductCardDTO;
import com.example.Product.DTO.ProductDTO;
import com.example.Product.DTO.UpdateProductDTO;
import com.example.Product.Entity.Product;
import com.example.Product.Service.ProductService;
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

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }

    @GetMapping("/{prodId}")
    public ResponseEntity<Product> getProductByID(@PathVariable Integer prodId){
        Product product = productService.getProductById(prodId);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @GetMapping("/card")
    public ResponseEntity <List<ProductCardDTO>> getAllProductCards(){
        List<ProductCardDTO> productCards = productService.getAllProductCardsDetails();
        return new ResponseEntity<>(productCards,HttpStatus.OK);
    }

    //get products
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {

        List<Product> products = productService.getProductsByCategory(category);
        return new ResponseEntity<>(products,HttpStatus.OK);

    }

    @GetMapping("/gender/{gender}")
    public ResponseEntity<List<Product>> getProductsByGender(@PathVariable String gender) {

        List<Product> products = productService.getProductsByGender(gender);
        return new ResponseEntity<>(products,HttpStatus.OK);

    }

    //create a product
    @PostMapping("/")
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductDTO newProduct,@RequestHeader("Authorization") String authHeader){

        String token = authHeader.substring(7);
        Product product = productService.addProduct(newProduct, token);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
    }

    //update a product
    @PatchMapping("/{prodId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer prodId, @Valid @RequestBody UpdateProductDTO productDTO, @RequestHeader("Authorization") String authHeader){

        String token = authHeader.substring(7);
        Product product = productService.updateProduct(prodId,productDTO,token);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    //delete a product
    @DeleteMapping("/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer prodId,@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        productService.deleteProduct(prodId,token);
        return new ResponseEntity<>("Product Deleted",HttpStatus.NO_CONTENT);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(@RequestBody FilterProductsDTO filters) {
        List<Product> products = productService.filterProducts(filters);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PatchMapping("/reduceStock/{prodId}")
    public void reduceStock(@RequestParam int quantity,@PathVariable Integer prodId){

        productService.reduceStock(prodId, quantity);

    }

}
