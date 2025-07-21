package com.example.demo.repository;

import com.example.demo.entites.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
    List<Book> findByCategoryIgnoreCase(String category);
    Optional<Book> findByTitle(String title);
}