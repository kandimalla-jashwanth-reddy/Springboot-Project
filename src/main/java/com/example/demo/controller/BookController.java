package com.example.demo.controller;

import com.example.demo.entites.Book;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Unified endpoint with all optional parameters
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category) {

        if (search != null && !search.isEmpty()) {
            return ResponseEntity.ok(bookService.searchBooks(search));
        }
        if (limit != null && limit > 0) {
            return ResponseEntity.ok(bookService.getBooksWithLimit(limit));
        }
        if (category != null && !category.isEmpty()) {
            return ResponseEntity.ok(bookService.getBooksByCategory(category));
        }
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping("/add")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book savedBook = bookService.saveBook(book);
        return ResponseEntity.ok(savedBook);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.getBookById(id);
        return book.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}