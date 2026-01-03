package com.example.ProductMicroServices.Clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Service
public class OrderClient {

    @Autowired
    private WebClient webClient;

    private final String baseUrl = "http://localhost:8082/order";

    private static final Logger logger = Logger.getLogger(OrderClient.class.getName());

    public boolean hasUserBoughtProduct(int productId, String authHeader) {
        String endpoint = baseUrl + "/product/" + productId;
        try {
            logger.info("Fetching hasUserBoughtProduct for " + productId);
            return Boolean.TRUE.equals(webClient.get()
                    .uri(endpoint)
                    .header("Authorization", authHeader)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> Mono.error(new RuntimeException("No such order found")))
                    .bodyToMono(Boolean.class) // Expect a boolean response
                    .defaultIfEmpty(false)
                    .onErrorReturn(false)
                    .block());
        } catch (Exception e) {
            logger.severe("Error fetching hasUserBoughtProduct: " + e.getMessage());
            throw new RuntimeException("Error checking product purchase status", e);
        }
    }
}
