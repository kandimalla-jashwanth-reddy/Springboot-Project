package com.example.demo.repository;


import com.example.demo.demo.Order;
import com.example.demo.demo.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
