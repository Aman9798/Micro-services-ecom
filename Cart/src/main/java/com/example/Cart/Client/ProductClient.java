package com.example.Cart.Client;

import com.example.Cart.DTO.ProductDTO;
import com.example.Cart.Services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Service
public class ProductClient {

    @Autowired
    private WebClient webClient;

    private final String baseUrl = "http://localhost:8081/products/";

    private static final Logger logger = Logger.getLogger(ProductClient.class.getName());

    public ProductDTO getProductById(int productId) {
        String endpoint = baseUrl + productId;
        try {
            logger.info("Fetching product with ID: " + productId);
            return webClient
                    .get()
                    .uri(endpoint)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            response -> Mono.error(new RuntimeException("Product not found"))
                    )
                    .bodyToMono(ProductDTO.class)
                    .block();
        } catch (Exception e) {
            logger.severe("Error fetching product: " + e.getMessage());
            return null;
        }
    }

    public void reduceStock(int quantity, int productId) {
        String endpoint = baseUrl + "reduceStock/" + productId + "?quantity=" + quantity;
        try {
            logger.info("Reducing stock for product ID: " + productId + " by quantity: " + quantity);
            webClient.patch()
                    .uri(endpoint)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> Mono.error(new RuntimeException("Product not found")))
                    .bodyToMono(ProductDTO.class)
                    .block();
            logger.info("Stock reduced successfully for product ID: " + productId);
        } catch (Exception e) {
            logger.severe("Error reducing stock: " + e.getMessage());
            throw e;
        }
    }
}
