package com.example.Cart.Services;

import com.example.Cart.DTO.CartItemDTO;
import com.example.Cart.Entity.Cart;
import com.example.Cart.Entity.CartItem;
import com.example.Cart.DTO.ProductDTO;
import com.example.Cart.Exception.NotFoundException;
import com.example.Cart.Repository.CartRepository;
import com.example.Cart.Repository.CartItemRepository;
import com.example.Cart.Utils.JwtTokenUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private WebClient webClient;

    public Cart getCartByUserId(int userId) {
        return cartRepository.findByUserID(userId);
    }


    public Cart removeCartItem(int cartItemId,String token) {

        String user = jwtTokenUtil.getUserId(token);
        int userId = Integer.parseInt(user);
        Cart cart = getCartByUserId(userId);
        boolean itemRemoved = cart.getCartItems().removeIf(item ->
                item.getCartItemId() == cartItemId);

        if (itemRemoved) {
            return cartRepository.save(cart);
        } else {
            throw new IllegalArgumentException("Cart item does not belong to the user's cart.");
        }
    }

    public Cart getCart(String token) {
        String user = jwtTokenUtil.getUserId(token);
        int userId = Integer.parseInt(user);
        return cartRepository.findByUserID(userId);
    }

    public Cart updateCartItem(int cartItemId,String token,int quantity) {

        String user = jwtTokenUtil.getUserId(token);
        int userId = Integer.parseInt(user);
        Cart userCart = getCartByUserId(userId);
        boolean itemExistsInCart = userCart.getCartItems().stream()
                .anyMatch(item -> item.getCartItemId() == cartItemId);



        if (!itemExistsInCart) {
            throw new RuntimeException("CartItem not found in user's cart");
        }
        CartItem existingItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("CartItem not found"));
        existingItem.setQuantity(quantity);
        cartItemRepository.save(existingItem);
        return existingItem.getCart();
    }


    @Transactional
    public Cart addCartItem(CartItemDTO cartItemDTO, String token) {
        String user = jwtTokenUtil.getUserId(token);
        int userId = Integer.parseInt(user);
        Cart cart = getCartByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserID(userId);
            cartRepository.save(cart);
        }

        boolean productExists = checkProductExists(cartItemDTO.getProductId());
        if (!productExists) {
            throw new NotFoundException("No such prodcut exists");
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
            newCartItem.setProductName(cartItemDTO.getProductName());
            newCartItem.setCart(cart);
            cartItemRepository.save(newCartItem);
        }
        entityManager.flush();
        entityManager.clear();
        return cartRepository.findByUserID(userId);
    }

    private boolean checkProductExists(int productId) {
        String productServiceUrl = "http://localhost:8081/products/" + productId;

        try {
            webClient.get()
                    .uri(productServiceUrl)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Product not found")))
                    .bodyToMono(ProductDTO.class)
                    .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}