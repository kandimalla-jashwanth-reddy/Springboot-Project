package com.example.demo.entites;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private Double price;
    private String isbn;
    private String description;
    private LocalDate publicationDate;
    private String category;

    // Default constructor
    public Book() {
    }

    // Parameterized constructor
    public Book(String title, String author, Double price, String isbn,
                String description, LocalDate publicationDate, String category) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.isbn = isbn;
        this.description = description;
        this.publicationDate = publicationDate;
        this.category = category;
    }

    // Getters and Setters for all fields
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}