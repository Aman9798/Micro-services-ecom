package com.example.Cart.Facade;

import com.example.Cart.Entity.Cart;
import com.example.Cart.Entity.CartItem;
import com.example.Cart.Exception.NotFoundException;
import com.example.Cart.Repository.CartItemRepository;
import com.example.Cart.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CartPersistenceFacade {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public Cart findCartByID(int cartId){
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("No cart present"));
    }

    public Cart save(Cart cart){
        return cartRepository.save(cart);
    }

    public Cart findCartByUserID(int userId){
        return cartRepository.findByUserID(userId);
    }

    public CartItem findCartItemByID(int cartItemId){
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
    }

    public CartItem save(CartItem cartItem){
        return cartItemRepository.save(cartItem);
    }

    public void deleteCartItemByProductId(int productId){
        cartItemRepository.deleteByProductId(productId);
    }

    public boolean findCartItemByProductId(int productId){
        return cartItemRepository.findByProductId(productId);
    }
}
