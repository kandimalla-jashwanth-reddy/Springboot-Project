package com.example.demo.repository;


import com.example.demo.entites.Book;
import com.example.demo.entites.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBook(Book book);
}
