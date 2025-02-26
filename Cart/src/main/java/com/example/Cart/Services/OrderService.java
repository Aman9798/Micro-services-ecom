package com.example.Cart.Services;


import com.example.Cart.DTO.CartItemDTO;
import com.example.Cart.DTO.ProductDTO;
import com.example.Cart.DTO.UserDTO;
import com.example.Cart.Entity.CartItem;
import com.example.Cart.Entity.OrderItem;
import com.example.Cart.Entity.Orders;
import com.example.Cart.Exception.NotFoundException;
import com.example.Cart.Repository.OrderItemRepository;
import com.example.Cart.Repository.OrderRepository;
import com.example.Cart.Utils.JwtTokenUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private WebClient webClient;

    @Autowired
    private OrderItemRepository orderItemRepo;

    @PersistenceContext
    private EntityManager entityManager;

    public Orders getAllOrders(String token) {
        String user = jwtTokenUtil.getUserId(token);
        int userId = Integer.parseInt(user);


        return orderRepo.findByUserId(userId);

    }


    @Transactional
    public Orders placeOrder(String token, List<CartItemDTO> cartItems) {
        String user = jwtTokenUtil.getUserId(token);
        int userId = Integer.parseInt(user);
        System.out.println(userId);
        Orders userOrders = new Orders();
        userOrders.setUserId(userId);
        orderRepo.save(userOrders);

        UserDTO userDetails = getUser(userId);

        if(userDetails == null){
            throw new NotFoundException("No such user found");
        }

        for(CartItemDTO cartItem : cartItems){
            ProductDTO product = getProducts(cartItem.getProductId());
            if(product == null){
                throw new NotFoundException("No such product present");
            }

            OrderItem newOrderItem = new OrderItem(product.getId(),
                                                    product.getName(),
                                                    product.getDescription(),
                                                    product.getPrice(),
                                                    cartItem.getQuantity(),
                                                    userDetails.getUserId(),
                                                    userDetails.getName(),
                                            "Temp address",
                                                    userDetails.getPhoneNumber());


            newOrderItem.setOrder(userOrders);
            orderItemRepo.save(newOrderItem);
        }
        entityManager.flush();
        entityManager.clear();
        return orderRepo.findByUserId(userId);
    }

    private ProductDTO getProducts(int productId) {
        String productServiceUrl = "http://localhost:8081/products/" + productId;

        try {
            ProductDTO product = webClient.get()
                    .uri(productServiceUrl)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Product not found")))
                    .bodyToMono(ProductDTO.class)
                    .block();
            return product;
        } catch (Exception e) {
            return null;
        }
    }

    private UserDTO getUser(int userId){
        String userServiceUrl = "http://localhost:8080/user/" + userId;
        try {
            UserDTO user = webClient.get()
                    .uri(userServiceUrl)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("User not found")))
                    .bodyToMono(UserDTO.class)
                    .block();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

}
