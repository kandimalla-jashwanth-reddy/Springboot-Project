package com.example.demo.service;


import com.example.demo.entites.Book;
import com.example.demo.entites.Review;
import com.example.demo.entites.User;
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

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review addReview(User user, Book book, Review review) {
        review.setUser(user);
        review.setBook(book);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsForBook(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        return reviewRepository.findByBook(book);
    }
}
