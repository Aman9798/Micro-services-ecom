package com.example.Cart.Services;

import com.example.Cart.DTO.*;
import com.example.Cart.Entity.Cart;
import com.example.Cart.Entity.CartItem;
import com.example.Cart.Entity.OrderItem;
import com.example.Cart.Entity.Orders;
import com.example.Cart.Exception.NotFoundException;
import com.example.Cart.Facade.CartPersistenceFacade;
import com.example.Cart.Facade.OrderPersistenceFacade;
import com.example.Cart.Mapper.OrderMapper;
import com.example.Cart.Utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
public class OrderService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WebClient webClient;

    @Autowired
    private OrderPersistenceFacade orderPersistenceFacade;

    @Autowired
    private CartPersistenceFacade cartPersistenceFacade;

    private static final Logger logger = Logger.getLogger(OrderService.class.getName());

    public List<OrdersDTO> getAllOrders(String token) {
        try {
            logger.info("Fetching all orders for token: " + token);
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);

            if (jwtTokenUtil.isAdmin(token)) {
                logger.info("User is admin, fetching all orders");
                List<Orders> userOrders = orderPersistenceFacade.findAllOrders();
                List<OrdersDTO> orders = userOrders.stream()
                        .map(OrderMapper::convertToOrderDTO)
                        .collect(Collectors.toList());
                logger.info("Fetched all orders successfully");
                return orders;
            } else {
                logger.info("User is not admin, fetching orders for user ID: " + userId);
                List<Orders> userOrders = orderPersistenceFacade.findOrdersByUserId(userId);
                List<OrdersDTO> orders = userOrders.stream()
                        .map(OrderMapper::convertToOrderDTO)
                        .collect(Collectors.toList());
                logger.info("Fetched orders for user ID: " + userId + " successfully");
                return orders;
            }
        } catch (Exception e) {
            logger.severe("Error fetching orders: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public List<OrdersDTO> placeOrder(String authHeader, AddressRequestDTO addressRequestDTO) {
        try {
            String token = authHeader.substring(7);
            logger.info("Placing order for token: " + token);
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);
            Orders userOrders = Orders.builder()
                    .userId(userId)
                    .orderItems(new ArrayList<>())
                    .build();
            orderPersistenceFacade.save(userOrders);
            logger.info("Created new order for user ID: " + userId);

            Cart userCart = cartPersistenceFacade.findCartByUserID(userId);
            List<CartItem> cartItems = userCart.getCartItems();

            if (cartItems == null || cartItems.isEmpty()) {
                logger.warning("No products in cart for user ID: " + userId);
                throw new NotFoundException("No Products in cart found");
            }
            UserDTO userDetails = getUser(userId);
            if (userDetails == null) {
                logger.warning("No such user found with ID: " + userId);
                throw new NotFoundException("No such user found");
            }
            int addressId = addressRequestDTO.getAddressId();
            for (CartItem cartItem : cartItems) {
                createOrderItem(userOrders, cartItem.getProductId(), cartItem.getQuantity(), userDetails, addressId, authHeader);
            }
            deleteCartItems(cartItems);

            List<Orders> orders = orderPersistenceFacade.findOrdersByUserId(userId);
            List<OrdersDTO> ordersDTOs = orders.stream()
                    .map(OrderMapper::convertToOrderDTO)
                    .collect(Collectors.toList());
            logger.info("Order placed successfully for user ID: " + userId);
            return ordersDTOs;
        } catch (Exception e) {
            logger.severe("Error placing order: " + e.getMessage());
            throw e;
        }
    }

    private void createOrderItem(Orders userOrders, int productId, int quantity, UserDTO userDetails, int addressId, String authHeader) {
        try {
            logger.info("Creating order item for product ID: " + productId + ", quantity: " + quantity);
            ProductDTO product = getProducts(productId);
            if (product == null) {
                logger.warning("No such product present with ID: " + productId);
                throw new NotFoundException("No such product present");
            }
            if (product.getStock() < quantity) {
                logger.warning("Required quantity not present in stock for product ID: " + productId);
                throw new NotFoundException("Required quantity not present in stock");
            }
            String token = authHeader.substring(7);
            logger.info("Placing order for token: " + token);
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);

            AddressDTO userAddress = fetchAddress(addressId, authHeader);
            String address = userAddress.toString();
            OrderItem newOrderItem = OrderItem.builder()
                    .createdAt(new Date())
                    .productId(product.getId())
                    .productName(product.getName())
                    .price(product.getPrice())
                    .quantity(quantity)
                    .userId(userId)
                    .userName(userDetails.getName())
                    .Address(address)
                    .phoneNumber(userDetails.getPhoneNumber())
                    .imageURL(product.getImageURL())
                    .order(userOrders)
                    .build();

            reduceStock(quantity, productId);
            orderPersistenceFacade.save(newOrderItem);
            userOrders.getOrderItems().add(newOrderItem);
            logger.info("Order item created successfully for product ID: " + productId);
        } catch (Exception e) {
            logger.severe("Error creating order item: " + e.getMessage());
            throw e;
        }
    }

    public List<OrdersDTO> buyNow(String authHeader, CartItemDTO cartItemDTO) {
        try {
            String token = authHeader.substring(7);
            logger.info("Processing buy now for token: " + token);
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);
            Orders userOrders = Orders.builder()
                    .userId(userId)
                    .orderItems(new ArrayList<>())
                    .build();
            orderPersistenceFacade.save(userOrders);
            logger.info("Created new order for user ID: " + userId);

            UserDTO userDetails = getUser(userId);
            if (userDetails == null) {
                logger.warning("No such user found with ID: " + userId);
                throw new NotFoundException("No such user found");
            }

            int addressId = cartItemDTO.getAddressId();
            createOrderItem(userOrders, cartItemDTO.getProductId(), cartItemDTO.getQuantity(), userDetails, addressId, authHeader);
            List<Orders> orders = orderPersistenceFacade.findOrdersByUserId(userId);
            List<OrdersDTO> ordersDTOs = orders.stream()
                    .map(OrderMapper::convertToOrderDTO)
                    .collect(Collectors.toList());

            CartItem newCartItem = CartItem.builder()
                    .productId(cartItemDTO.getProductId())
                    .quantity(cartItemDTO.getQuantity())
                    .build();;

            logger.info("Buy now processed successfully for user ID: " + userId);
            return ordersDTOs;
        } catch (Exception e) {
            logger.severe("Error processing buy now: " + e.getMessage());
            throw e;
        }
    }

    private ProductDTO getProducts(int productId) {
        String productServiceUrl = "http://localhost:8081/products/" + productId;
        try {
            logger.info("Fetching product with ID: " + productId);
            ProductDTO product = webClient.get()
                    .uri(productServiceUrl)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Product not found")))
                    .bodyToMono(ProductDTO.class)
                    .block();
            logger.info("Fetched product successfully with ID: " + productId);
            return product;
        } catch (Exception e) {
            logger.severe("Error fetching product: " + e.getMessage());
            throw e;
        }
    }

    private UserDTO getUser(int userId) {
        String userServiceUrl = "http://localhost:8080/user/" + userId;
        try {
            logger.info("Fetching user with ID: " + userId);
            UserDTO user = webClient.get()
                    .uri(userServiceUrl)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("User not found")))
                    .bodyToMono(UserDTO.class)
                    .block();
            logger.info("Fetched user successfully with ID: " + userId);
            return user;
        } catch (Exception e) {
            logger.severe("Error fetching user: " + e.getMessage());
            throw e;
        }
    }

    private void reduceStock(int quantity, int productId) {
        String productServiceUrl = "http://localhost:8081/products/reduceStock/" + productId + "?quantity=" + quantity;
        try {
            logger.info("Reducing stock for product ID: " + productId + " by quantity: " + quantity);
            webClient.patch()
                    .uri(productServiceUrl)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Product not found")))
                    .bodyToMono(ProductDTO.class)
                    .block();
            logger.info("Stock reduced successfully for product ID: " + productId);
        } catch (Exception e) {
            logger.severe("Error reducing stock: " + e.getMessage());
            throw e;
        }
    }

    private void deleteCartItems(List<CartItem> cartItems) {
        try {
            logger.info("Deleting cart items");
            for (CartItem cartItem : cartItems) {
                cartPersistenceFacade.deleteCartItemByProductId(cartItem.getProductId());
            }
            logger.info("Cart items deleted successfully");
        } catch (Exception e) {
            logger.severe("Error deleting cart items: " + e.getMessage());
            throw e;
        }
    }

    private AddressDTO fetchAddress(int addressId, String authHeader) {
        String userServiceUrl = "http://localhost:8080/user/address/" + addressId;
        try {
            logger.info("Fetching address with ID: " + addressId);
            AddressDTO address = webClient.get()
                    .uri(userServiceUrl)
                    .headers(headers -> headers.set("Authorization", authHeader))
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Address not found")))
                    .bodyToMono(AddressDTO.class)
                    .block();
            logger.info("Fetched address successfully with ID: " + addressId);
            return address;
        } catch (Exception e) {
            logger.severe("Error fetching address: " + e.getMessage());
            throw e;
        }
    }

    public OrdersDTO getOrderById(int orderId, String token) {
        try {
            logger.info("Fetching order with ID: " + orderId);
            Orders userOrder = orderPersistenceFacade.findOrderById(orderId);
            String user = jwtTokenUtil.getUserId(token);
            boolean isAdmin = jwtTokenUtil.isAdmin(token);
            int userId = Integer.parseInt(user);
            if (userOrder.getUserId() != userId && !isAdmin ) {
                logger.warning("Order doesn't belong to the user with ID: " + userId);
                throw new RuntimeException("Order doesn't belong to the user");
            }
            logger.info("Fetched order successfully with ID: " + orderId);
            return OrderMapper.convertToOrderDTO(userOrder);
        } catch (Exception e) {
            logger.severe("Error fetching order: " + e.getMessage());
            throw e;
        }
    }

    public boolean checkUserHasBoughtProduct(String token, int productId) {

        try{
            String userId = jwtTokenUtil.getUserId(token);
            int userID = Integer.parseInt(userId);
            //System.out.println(userID);
            List<OrderItem> orderItems =  orderPersistenceFacade.findOrderItemsByUserIdAndProductId(userID, productId);
        //    System.out.println(!orderItems.isEmpty());
            return (!orderItems.isEmpty());

        }catch(Exception e){
            throw e;
        }
    }
}