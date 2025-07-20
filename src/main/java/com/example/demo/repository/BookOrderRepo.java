package com.example.demo.repository;

import com.example.demo.entites.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookOrderRepo extends JpaRepository<BookOrder ,Long> {
}
