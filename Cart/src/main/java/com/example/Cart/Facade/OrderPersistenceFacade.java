package com.example.Cart.Facade;

import com.example.Cart.Entity.Cart;
import com.example.Cart.Entity.OrderItem;
import com.example.Cart.Entity.Orders;
import com.example.Cart.Exception.NotFoundException;
import com.example.Cart.Repository.OrderItemRepository;
import com.example.Cart.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderPersistenceFacade {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    public Orders findOrderById(int orderId){
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("No such order Found"));
    }

    public List<Orders> findOrdersByUserId(int userId){
        return orderRepository.findByUserId(userId);
    }

    public Orders save(Orders order){
        return orderRepository.save(order);
    }

    public List<Orders> findAllOrders(){
        return orderRepository.findAll();
    }

    public OrderItem findOrderItemByID(int orderItemId){
        return orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new NotFoundException("No order item present"));
    }

    public List<OrderItem> findOrderItemsByUserIdAndProductId(int userID, int productId){
        return orderItemRepository.findByUserIdAndProductId(userID, productId);
    }

    public OrderItem save(OrderItem orderItem){
        return orderItemRepository.save(orderItem);
    }
}
