package com.example.Cart.Delegate;

import com.example.Cart.DTO.AddressDTO;
import com.example.Cart.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Service
public class UserDelegate {

    @Autowired
    private WebClient webClient;

    private final String baseUrl = "http://localhost:8080/user/";

    private static final Logger logger = Logger.getLogger(UserDelegate.class.getName());

    public UserDTO getUserFromUserId(int userId) {
        String endpoint = baseUrl + userId;
        try {
            logger.info("Fetching user with ID: " + userId);
            UserDTO user = webClient.get()
                    .uri(endpoint)
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> Mono.error(new RuntimeException("User not found")))
                    .bodyToMono(UserDTO.class)
                    .block();
            logger.info("Fetched user successfully with ID: " + userId);
            return user;
        } catch (Exception e) {
            logger.severe("Error fetching user: " + e.getMessage());
            return null;
        }
    }

    public AddressDTO fetchAddress(int addressId, String authHeader) {
        String endpoint = baseUrl + "address/" + addressId;
        try {
            logger.info("Fetching address with ID: " + addressId);
            AddressDTO address = webClient.get()
                    .uri(endpoint)
                    .headers(headers -> headers.set("Authorization", authHeader))
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            clientResponse -> Mono.error(new RuntimeException("Address not found")))
                    .bodyToMono(AddressDTO.class)
                    .block();
            logger.info("Fetched address successfully with ID: " + addressId);
            return address;
        } catch (Exception e) {
            logger.severe("Error fetching address: " + e.getMessage());
            throw e;
        }
    }
}
