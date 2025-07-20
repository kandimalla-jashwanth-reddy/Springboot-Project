package com.example.demo.controller;

import com.example.demo.entites.Order;
import com.example.demo.entites.OrderItem;
import com.example.demo.entites.User;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    // Place an order with full user and item details
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
        Order placedOrder = orderService.placeOrder(order.getUser(), order.getItems());
        return ResponseEntity.ok(placedOrder);
    }

    // ✅ Place an order using userId and list of bookIds
    @PostMapping("/place-by-ids")
    public ResponseEntity<List<String>> placeOrderByIds(
            @RequestParam Long userId,
            @RequestBody List<Long> bookIds
    ) {
        List<String> orderedTitles = orderService.placeOrders(userId, bookIds);
        return ResponseEntity.ok(orderedTitles);
    }

    // Get all orders of a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        List<Order> orders = orderService.getOrdersByUser(user);
        return ResponseEntity.ok(orders);
    }
}
