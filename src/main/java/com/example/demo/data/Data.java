package com.example.demo.data;
import com.example.demo.demo.*;
import com.example.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;
public class Data
{
    @Bean
    CommandLineRunner initData(UserRepository userRepo,
                               BookRepository bookRepo,
                               ReviewRepository reviewRepo,
                               OrderRepository orderRepo,
                               OrderItemRepository orderItemRepo) {
        return args -> {

            // Create sample user
            User user = new User();
            user.setName("Sampath");
            user.setEmail("sampath@example.com");
            user.setPassword("password123");
            user.setRole("USER");
            userRepo.save(user);

            // Create sample book
            Book book1 = new Book();
            book1.setTitle("Clean Code");
            book1.setAuthor("Robert C. Martin");
            book1.setPrice(450.0);
            book1.setIsbn("9780132350884");
            book1.setDescription("A Handbook of Agile Software Craftsmanship");
            book1.setPublicationDate(LocalDate.of(2008, 8, 1));
            bookRepo.save(book1);

            Book book2 = new Book();
            book2.setTitle("Effective Java");
            book2.setAuthor("Joshua Bloch");
            book2.setPrice(550.0);
            book2.setIsbn("9780134685991");
            book2.setDescription("Best practices for Java programming.");
            book2.setPublicationDate(LocalDate.of(2018, 1, 6));
            bookRepo.save(book2);

            // Add review
            Review review = new Review();
            review.setBook(book1);
            review.setUser(user);
            review.setRating(5);
            review.setComment("One of the best books for clean coding!");
            reviewRepo.save(review);

            // Create order
            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDate.now());
            order = orderRepo.save(order);

            OrderItem item = new OrderItem();
            item.setBook(book1);
            item.setQuantity(1);
            item.setOrder(order);
            orderItemRepo.save(item);

            System.out.println("✅ Sample data inserted!");
        };
    }
}

