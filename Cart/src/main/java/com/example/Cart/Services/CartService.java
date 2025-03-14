package com.example.Cart.Services;

import com.example.Cart.DTO.CartDTO;
import com.example.Cart.DTO.CartItemDTO;
import com.example.Cart.Entity.Cart;
import com.example.Cart.Entity.CartItem;
import com.example.Cart.DTO.ProductDTO;
import com.example.Cart.Exception.NotFoundException;
import com.example.Cart.Repository.CartRepository;
import com.example.Cart.Repository.CartItemRepository;
import com.example.Cart.Utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.logging.Logger;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WebClient webClient;

    private static final Logger logger = Logger.getLogger(CartService.class.getName());

    public Cart getCartByUserId(int userId) {
        try {
            logger.info("Fetching cart for user ID: " + userId);
            return cartRepository.findByUserID(userId);
        } catch (Exception e) {
            logger.severe("Error fetching cart by user ID: " + e.getMessage());
            throw new RuntimeException("Error fetching cart by user ID", e);
        }
    }

    public CartDTO getCart(String token) {
        try {
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);
            logger.info("Fetching cart for user ID: " + userId);
            Cart cart = cartRepository.findByUserID(userId);
            CartDTO userCart = new CartDTO(cart.getCartId(), cart.getCartItems(),cart.getTotalPrice());
            return userCart;
        } catch (Exception e) {
            logger.severe("Error fetching cart: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public CartDTO updateCartItem(int cartItemId, String token, int quantity) {
        try {
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);
            logger.info("Updating cart item ID: " + cartItemId + " for user ID: " + userId + " with quantity: " + quantity);
            Cart userCart = getCartByUserId(userId);
            boolean itemExistsInCart = userCart.getCartItems().stream()
                    .anyMatch(item -> item.getCartItemId() == cartItemId);

            if (!itemExistsInCart) {
                logger.warning("CartItem not found in user's cart");
                throw new RuntimeException("CartItem not found in user's cart");
            }
            CartItem existingItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("CartItem not found"));
            if (quantity < 0) {
                logger.warning("Quantity cannot be less than or equal to 0");
                throw new RuntimeException("Quantity cannot be less than or equal to 0");
            } else if (quantity == 0) {
                userCart.getCartItems().removeIf(item -> item.getCartItemId() == cartItemId);
            } else {
                existingItem.setQuantity(quantity);
                cartItemRepository.save(existingItem);
            }
            cartRepository.save(userCart);
            Cart cart = existingItem.getCart();
            return new CartDTO(cart.getCartId(), cart.getCartItems(),cart.getTotalPrice());
        } catch (RuntimeException e) {
            logger.severe("Error updating cart item: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public List<CartItem> addCartItem(CartItemDTO cartItemDTO, String token) {
        try {
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);
            logger.info("Adding cart item for user ID: " + userId);
            Cart cart = getCartByUserId(userId);
            if (cart == null) {
                cart = new Cart();
                cart.setUserID(userId);
                cartRepository.save(cart);
            }

            ProductDTO product = getProducts(cartItemDTO.getProductId());
            if (product == null) {
                logger.warning("No such product exists");
                throw new NotFoundException("No such product exists");
            }

            Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProductId() == cartItemDTO.getProductId())
                    .findFirst();

            if (!existingCartItem.isEmpty()) {
                CartItem cartItem = existingCartItem.get();
                cartItem.setCart(cart);
                cartItem.setQuantity(cartItem.getQuantity() + cartItemDTO.getQuantity());
            } else {
                CartItem newCartItem = new CartItem();
                newCartItem.setProductId(cartItemDTO.getProductId());
                newCartItem.setQuantity(cartItemDTO.getQuantity());
                newCartItem.setProductName(product.getName());
                newCartItem.setCart(cart);
                newCartItem.setPrice(product.getPrice());
                newCartItem.setImageUrl(product.getImageURL());
                cartItemRepository.save(newCartItem);
                cart.getCartItems().add(newCartItem);
            }
            return cart.getCartItems();
        } catch (Exception e) {
            logger.severe("Error adding cart item: " + e.getMessage());
            throw e;
        }
    }

    private ProductDTO getProducts(int productId) {
        String productServiceUrl = "http://localhost:8081/products/" + productId;
        try {
            logger.info("Fetching product with ID: " + productId);
            return webClient.get()
                    .uri(productServiceUrl)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Product not found")))
                    .bodyToMono(ProductDTO.class)
                    .block();
        } catch (Exception e) {
            logger.severe("Error fetching product: " + e.getMessage());
            return null;
        }
    }

    public List<CartItem> getCartByCartId(int cartId) {
        try {
            logger.info("Fetching cart with ID: " + cartId);
            Cart userCart = cartRepository.findById(cartId).orElseThrow(() -> new NotFoundException("No cart present"));
            return userCart.getCartItems();
        } catch (NotFoundException e) {
            logger.warning("No cart present: " + e.getMessage());
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            logger.severe("Error fetching cart by cart ID: " + e.getMessage());
            throw new RuntimeException("Error fetching cart by cart ID", e);
        }
    }
}