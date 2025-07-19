package com.example.demo.controller;

import com.example.demo.entites.Order;
import com.example.demo.entites.OrderItem;
import com.example.demo.entites.User;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public Order placeOrder(@RequestBody Order order) {
        User user = order.getUser();
        List<OrderItem> items = order.getItems();
        return orderService.placeOrder(user, items);
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrders(@PathVariable Long userId) {
        User user = new User();
        user.setId(userId);
        return orderService.getOrdersByUser(user);
    }
}
