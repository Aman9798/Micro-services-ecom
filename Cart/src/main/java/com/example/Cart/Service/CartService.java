package com.example.Cart.Service;

import com.example.Cart.DTO.CartItemResponseDTO;
import com.example.Cart.Delegate.ProductDelegate;
import com.example.Cart.DTO.CartResponseDTO;
import com.example.Cart.DTO.CartItemRequestDTO;
import com.example.Cart.Entity.Cart;
import com.example.Cart.Entity.CartItem;
import com.example.Cart.DTO.ProductDTO;
import com.example.Cart.Exception.NotFoundException;
import com.example.Cart.Facade.CartPersistenceFacade;
import com.example.Cart.Mapper.CartMapper;
import com.example.Cart.Utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.logging.Logger;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartPersistenceFacade cartPersistenceFacade;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ProductDelegate productDelegate;

    private static final Logger logger = Logger.getLogger(CartService.class.getName());

    public Cart getCartByUserId(int userId) {
        try {
            logger.info("Fetching cart for user ID: " + userId);
            return cartPersistenceFacade.findCartByUserID(userId);
        } catch (Exception e) {
            logger.severe("Error fetching cart by user ID: " + e.getMessage());
            throw new RuntimeException("Error fetching cart by user ID", e);
        }
    }

    public CartResponseDTO getCart(String token) {
        try {
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);
            logger.info("Fetching cart for user ID: " + userId);
            Cart cart = cartPersistenceFacade.findCartByUserID(userId);

            if (cart == null) {
                cart = Cart.builder()
                        .userID(userId)
                        .cartItems(new ArrayList<>())
                        .build();
                cart = cartPersistenceFacade.save(cart);
            }

            return CartMapper.convertToCartResponseDTO(cart);
        } catch (Exception e) {
            logger.severe("Error fetching cart: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public CartResponseDTO updateCartItem(int cartItemId,  int quantity, String token) {
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
            CartItem existingItem = cartPersistenceFacade.findCartItemByID(cartItemId);
            if (quantity < 0) {
                logger.warning("Quantity cannot be less than or equal to 0");
                throw new RuntimeException("Quantity cannot be less than or equal to 0");
            } else if (quantity == 0) {
                userCart.getCartItems().removeIf(item -> item.getCartItemId() == cartItemId);
            } else {
                existingItem.setQuantity(quantity);
                cartPersistenceFacade.save(existingItem);
            }
            cartPersistenceFacade.save(userCart);
            Cart cart = existingItem.getCart();
            return CartMapper.convertToCartResponseDTO(cart);
        } catch (RuntimeException e) {
            logger.severe("Error updating cart item: " + e.getMessage());
            throw e;
        }
    }

    @Transactional
    public List<CartItemResponseDTO> addCartItem(CartItemRequestDTO cartItemRequest, String token) {
        try {
            String user = jwtTokenUtil.getUserId(token);
            int userId = Integer.parseInt(user);
            logger.info("Adding cart item for user ID: " + userId);
            Cart cart = getCartByUserId(userId);
            if (cart == null) {
                cart = Cart.builder()
                        .userID(userId)
                        .cartItems(new ArrayList<>())
                        .build();

                cartPersistenceFacade.save(cart);
            }

            ProductDTO product = productDelegate.getProductById(cartItemRequest.getProductId());
            if (product == null) {
                logger.warning("No such product exists");
                throw new NotFoundException("No such product exists");
            }

            Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProductId() == cartItemRequest.getProductId())
                    .findFirst();

            if (!existingCartItem.isEmpty()) {
                CartItem cartItem = existingCartItem.get();
                cartItem.setCart(cart);
                cartItem.setQuantity(cartItem.getQuantity() + cartItemRequest.getQuantity());
            } else {
                CartItem newCartItem = CartItem.builder()
                        .productName(product.getName())
                        .productId(cartItemRequest.getProductId())
                        .cart(cart)
                        .quantity(cartItemRequest.getQuantity())
                        .price(product.getPrice())
                        .imageUrl(product.getImageURL())
                        .build();

                cartPersistenceFacade.save(newCartItem);
                cart.getCartItems().add(newCartItem);
            }

            List<CartItem> cartItems = cart.getCartItems();
            return cartItems.stream()
                    .map(CartMapper::convertToCartItemResponseDTO)
                    .toList();
        } catch (Exception e) {
            logger.severe("Error adding cart item: " + e.getMessage());
            throw e;
        }
    }

    public List<CartItem> getCartByCartId(int cartId) {
        try {
            logger.info("Fetching cart with ID: " + cartId);
            Cart userCart = cartPersistenceFacade.findCartByID(cartId);
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