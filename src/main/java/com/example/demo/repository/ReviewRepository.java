package com.example.demo.repository;


import com.example.demo.demo.Book;
import com.example.demo.demo.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBook(Book book);
}
