package com.example.demo.data;
import com.example.demo.entites.*;
import com.example.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class Data {
    @Bean
    CommandLineRunner initData(UserRepository userRepo,
                               BookRepository bookRepo,
                               ReviewRepository reviewRepo,
                               OrderRepository orderRepo,
                               OrderItemRepository orderItemRepo) {
        return args -> {
            // Remove sample user and related data creation
            // If you want to add sample books, do so here without linking to a user
        };
    }
}