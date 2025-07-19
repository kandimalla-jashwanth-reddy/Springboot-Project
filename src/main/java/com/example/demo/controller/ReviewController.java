package com.example.demo.controller;

import com.example.demo.entites.Book;
import com.example.demo.entites.Review;
import com.example.demo.entites.User;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/{bookId}/add")
    public Review addReview(@PathVariable Long bookId, @RequestBody Review review) {
        User user = review.getUser();
        Book book = new Book();
        book.setId(bookId);
        return reviewService.addReview(user, book, review);
    }

    @GetMapping("/{bookId}")
    public List<Review> getReviews(@PathVariable Long bookId) {
        return reviewService.getReviewsForBook(bookId);
    }
}
