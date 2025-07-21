package com.example.demo.service;

import com.example.demo.entites.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface BookService {
    List<Book> getAllBooks();
    List<Book> getBooksWithLimit(int limit);
    List<Book> searchBooks(String query);
    List<Book> getBooksByCategory(String category);
    Book saveBook(Book book);
    Optional<Book> getBookById(Long id);
    void deleteBook(Long id);
}

@Service
class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getBooksWithLimit(int limit) {
        return bookRepository.findAll().stream()
                .limit(limit)
                .toList();
    }

    @Override
    public List<Book> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }

    @Override
    public List<Book> getBooksByCategory(String category) {
        // Implement your category filtering logic here
        // For example, if you have a category field in Book entity:
        return bookRepository.findByCategoryIgnoreCase(category);

        // If you don't have categories yet, you can temporarily return all books:
        // return bookRepository.findAll();
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}