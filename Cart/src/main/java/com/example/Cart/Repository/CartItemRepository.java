package com.example.Cart.Repository;

import com.example.Cart.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

    void deleteByProductId(int productId);

    boolean findByProductId(int productId);
}
