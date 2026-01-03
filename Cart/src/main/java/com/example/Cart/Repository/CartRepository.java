package com.example.Cart.Repository;

import com.example.Cart.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Integer> {

    Cart findByUserID(int userId);
}
