package com.example.demo.controller;

import com.example.demo.entites.Book;
import com.example.demo.entites.Review;
import com.example.demo.entites.User;
import com.example.demo.service.BookService;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final BookService bookService;

    @Autowired
    public ReviewController(ReviewService reviewService, BookService bookService) {
        this.reviewService = reviewService;
        this.bookService = bookService;
    }

    @PostMapping("/{bookId}/add")
    public Review addReview(@PathVariable Long bookId, @RequestBody Review review) {
        Book book = bookService.getBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        User user = review.getUser(); // Make sure user is properly set in the request
        return reviewService.addReview(user, book, review);
    }

    @GetMapping("/{bookId}")
    public List<Review> getReviews(@PathVariable Long bookId) {
        return reviewService.getReviewsForBook(bookId);
    }
}