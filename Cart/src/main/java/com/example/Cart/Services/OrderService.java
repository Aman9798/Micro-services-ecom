package com.example.Cart.Services;

import com.example.Cart.DTO.*;
import com.example.Cart.Entity.Cart;
import com.example.Cart.Entity.CartItem;
import com.example.Cart.Entity.OrderItem;
import com.example.Cart.Entity.Orders;
import com.example.Cart.Exception.NotFoundException;
import com.example.Cart.Repository.CartItemRepository;
import com.example.Cart.Repository.CartRepository;
import com.example.Cart.Repository.OrderItemRepository;
import com.example.Cart.Repository.OrderRepository;
import com.example.Cart.Utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private CartItemRepository cartRepo;


    @Autowired
    private CartRepository cartRepository;

    public List<OrdersDTO> getAllOrders(String token) {
        try {
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);

            if(jwtTokenUtil.isAdmin(token)){
                List<Orders> userOrders = orderRepo.findAll();
                List<OrdersDTO> orders = userOrders.stream().map(
                        userOrder-> new OrdersDTO(
                                userOrder.getOrderId(),
                                userOrder.getOrderItems(),
                                userOrder.getTotalPrice()
                        )).collect(Collectors.toList());

                return orders;
            }
            else{
            List<Orders> userOrders = orderRepo.findByUserId(userId);
            List<OrdersDTO> orders = userOrders.stream().map(
                    userOrder-> new OrdersDTO(
                            userOrder.getOrderId(),
                            userOrder.getOrderItems(),
                            userOrder.getTotalPrice()
                    )).collect(Collectors.toList());

            return orders;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Transactional
    public List<OrdersDTO> placeOrder(String authHeader,AddressRequestDTO addressRequestDTO) {
        try {
            String token = authHeader.substring(7);
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);
            Orders userOrders = new Orders();
            userOrders.setUserId(userId);
            orderRepo.save(userOrders);
            Cart userCart = cartRepository.findByUserID(userId);
            List<CartItem> cartItems = userCart.getCartItems();

            if (cartItems == null || cartItems.isEmpty()) {
                throw new NotFoundException("No Products in cart found");
            }
            UserDTO userDetails = getUser(userId);
            if (userDetails == null) {
                throw new NotFoundException("No such user found");
            }
            int addressId = addressRequestDTO.getAddressId();
            for (CartItem cartItem : cartItems) {
                createOrderItem(userOrders, cartItem.getProductId(), cartItem.getQuantity(), userDetails,addressId,authHeader);
            }
            deleteCartItems(cartItems);

            List<Orders> orders = orderRepo.findByUserId(userId);
            List<OrdersDTO> ordersDTOs = orders.stream().map(
                    userOrder-> new OrdersDTO(
                            userOrder.getOrderId(),
                            userOrder.getOrderItems(),
                            userOrder.getTotalPrice()
                    )).collect(Collectors.toList());

            return ordersDTOs;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void createOrderItem(Orders userOrders, int productId, int quantity, UserDTO userDetails,int addressId,String authHeader) {
        ProductDTO product = getProducts(productId);
        if (product == null) {
            throw new NotFoundException("No such product present");
        }
        if (product.getStock() < quantity) {
            throw new NotFoundException("Required quantity not present in stock");
        }

        AddressDTO userAddress = fetchAddress(addressId,authHeader);
        String address = userAddress.toString();
        OrderItem newOrderItem = new OrderItem(product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                quantity,
                userDetails.getUserId(),
                userDetails.getName(),
                address,
                userDetails.getPhoneNumber(),
                product.getImageURL());
        reduceStock(quantity, productId);
        newOrderItem.setOrder(userOrders);
        orderItemRepo.save(newOrderItem);
        userOrders.getOrderItems().add(newOrderItem);

    }

    public List<OrdersDTO> buyNow(String authHeader, CartItemDTO cartItemDTO) {
        try {
            String token = authHeader.substring(7);
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);
            Orders userOrders = new Orders();
            userOrders.setUserId(userId);
            orderRepo.save(userOrders);
            UserDTO userDetails = getUser(userId);
            if (userDetails == null) {
                throw new NotFoundException("No such user found");
            }

            int addressId = cartItemDTO.getAddressId();
            createOrderItem(userOrders, cartItemDTO.getProductId(), cartItemDTO.getQuantity(), userDetails,addressId,authHeader);
            List<Orders> orders = orderRepo.findByUserId(userId);
            List<OrdersDTO> ordersDTOs = orders.stream().map(
                    userOrder-> new OrdersDTO(
                            userOrder.getOrderId(),
                            userOrder.getOrderItems(),
                            userOrder.getTotalPrice()
                    )).collect(Collectors.toList());

            return ordersDTOs;
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
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
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private UserDTO getUser(int userId) {
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
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void reduceStock(int quantity, int productId) {
        String productServiceUrl = "http://localhost:8081/products/reduceStock/" + productId + "?quantity=" + quantity;
        try {
            webClient.patch()
                    .uri(productServiceUrl)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Product not found")))
                    .bodyToMono(ProductDTO.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void deleteCartItems(List<CartItem> cartItems) {
        try {
            for (CartItem cartItem : cartItems) {
                cartRepo.deleteByProductId(cartItem.getProductId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private AddressDTO fetchAddress(int addressId,String authHeader){
        String userServiceUrl = "http://localhost:8080/user/address/" + addressId;
        try {
            AddressDTO address = webClient.get()
                    .uri(userServiceUrl)
                    .headers(headers -> headers.set("Authorization", authHeader))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Address not found")))
                    .bodyToMono(AddressDTO.class)
                    .block();
            return address;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public OrdersDTO getOrderById(int orderId,String token) {

        Orders userOrder = orderRepo.findById(orderId).orElseThrow(()-> new NotFoundException("No such order Found"));
        String user = jwtTokenUtil.getUserId(token);
        int userId = Integer.parseInt(user);
        if(userOrder.getUserId() != userId){
            throw new RuntimeException("Order doesn't belongs to the user");
        }

        return new OrdersDTO(userOrder.getOrderId(), userOrder.getOrderItems(), userOrder.getTotalPrice());


    }
}