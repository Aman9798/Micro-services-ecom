package com.example.Cart.Repository;

import com.example.Cart.Entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
    List<OrderItem> findByUserIdAndProductId(int userID, int productId);
}
