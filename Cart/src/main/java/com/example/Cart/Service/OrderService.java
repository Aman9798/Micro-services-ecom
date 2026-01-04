package com.example.Cart.Service;

import com.example.Cart.Delegate.ProductDelegate;
import com.example.Cart.Delegate.UserDelegate;
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
import org.springframework.stereotype.Service;

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
    private UserDelegate userDelegate;

    @Autowired
    private ProductDelegate productDelegate;

    @Autowired
    private OrderPersistenceFacade orderPersistenceFacade;

    @Autowired
    private CartPersistenceFacade cartPersistenceFacade;

    private static final Logger logger = Logger.getLogger(OrderService.class.getName());

    public List<OrderResponseDTO> getAllOrders(String token) {
        try {
            if (!jwtTokenUtil.isAdmin(token)) {
                logger.warning("Unauthorized access attempt to delete product");
                throw new RuntimeException("This is an admin functionality");
            }

            logger.info("User is admin, fetching all orders for " + token);

            List<Orders> allOrders = orderPersistenceFacade.findAllOrders();
            logger.info("Fetched all orders successfully");

            return allOrders.stream()
                    .map(OrderMapper::convertToOrderResponseDTO)
                    .toList();
        } catch (Exception e) {
            logger.severe("Error fetching orders: " + e.getMessage());
            throw e;
        }
    }

    public List<OrderResponseDTO> getAllUserOrders(String token) {
        try {
            logger.info("Fetching all orders for token: " + token);
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);

            logger.info("Fetching orders for user ID: " + userId);

            List<Orders> userOrders = orderPersistenceFacade.findOrdersByUserId(userId);
            logger.info("Fetched orders for user ID: " + userId + " successfully");

            return userOrders.stream()
                    .map(OrderMapper::convertToOrderResponseDTO)
                    .toList();
        } catch (Exception e) {
            logger.severe("Error fetching orders: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public List<OrderResponseDTO> placeOrder(AddressRequestDTO addressRequestDTO, String authHeader) {
        try {
            String token = jwtTokenUtil.getToken(authHeader);
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
            UserDTO userDetails = userDelegate.getUserFromUserId(userId);
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
            logger.info("Order placed successfully for user ID: " + userId);

            return orders.stream()
                    .map(OrderMapper::convertToOrderResponseDTO)
                    .toList();
        } catch (Exception e) {
            logger.severe("Error placing order: " + e.getMessage());
            throw e;
        }
    }

    private void createOrderItem(Orders userOrders, int productId, int quantity, UserDTO userDetails, int addressId, String authHeader) {
        try {
            logger.info("Creating order item for product ID: " + productId + ", quantity: " + quantity);
            ProductDTO product = productDelegate.getProductById(productId);
            if (product == null) {
                logger.warning("No such product present with ID: " + productId);
                throw new NotFoundException("No such product present");
            }
            if (product.getStock() < quantity) {
                logger.warning("Required quantity not present in stock for product ID: " + productId);
                throw new NotFoundException("Required quantity not present in stock");
            }
            String token = jwtTokenUtil.getToken(authHeader);
            logger.info("Placing order for token: " + token);
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);

            AddressDTO userAddress = userDelegate.fetchAddress(addressId, authHeader);
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

            productDelegate.reduceStock(quantity, productId);
            orderPersistenceFacade.save(newOrderItem);

            userOrders.getOrderItems().add(newOrderItem);
            logger.info("Order item created successfully for product ID: " + productId);
        } catch (Exception e) {
            logger.severe("Error creating order item: " + e.getMessage());
            throw e;
        }
    }

    public List<OrderResponseDTO> buyNow(BuyNowRequestDTO buyNowRequestDTO, String authHeader) {
        try {
            String token = jwtTokenUtil.getToken(authHeader);
            logger.info("Processing buy now for token: " + token);
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);
            Orders userOrders = Orders.builder()
                    .userId(userId)
                    .orderItems(new ArrayList<>())
                    .build();
            orderPersistenceFacade.save(userOrders);
            logger.info("Created new order for user ID: " + userId);

            UserDTO userDetails = userDelegate.getUserFromUserId(userId);
            if (userDetails == null) {
                logger.warning("No such user found with ID: " + userId);
                throw new NotFoundException("No such user found");
            }

            int addressId = buyNowRequestDTO.getAddressId();
            createOrderItem(userOrders, buyNowRequestDTO.getProductId(), buyNowRequestDTO.getQuantity(), userDetails, addressId, authHeader);

            List<Orders> orders = orderPersistenceFacade.findOrdersByUserId(userId);
            logger.info("Buy now processed successfully for user ID: " + userId);

            return orders.stream()
                    .map(OrderMapper::convertToOrderResponseDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.severe("Error processing buy now: " + e.getMessage());
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


    public OrderResponseDTO getOrderById(int orderId, String token) {
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
            return OrderMapper.convertToOrderResponseDTO(userOrder);
        } catch (Exception e) {
            logger.severe("Error fetching order: " + e.getMessage());
            throw e;
        }
    }

    public boolean checkUserHasBoughtProduct(int productId, String token) {

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