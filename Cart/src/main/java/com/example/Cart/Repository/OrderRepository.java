package com.example.Cart.Repository;


import com.example.Cart.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Integer> {
    Orders findByUserId(int userId);
}
