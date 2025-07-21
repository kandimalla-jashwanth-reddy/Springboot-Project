package com.example.demo.service;


import com.example.demo.entites.Book;
import com.example.demo.entites.Review;
import com.example.demo.entites.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ReviewService {
    Review addReview(User user, Book book, Review review);
    List<Review> getReviewsForBook(Long bookId);
}

@Service
 class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Review addReview(User user, Book book, Review review) {
        review.setUser(user);
        review.setBook(book);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsForBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return reviewRepository.findByBook(book);
    }
}