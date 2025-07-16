package com.example.demo.demo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private Double price;

    private String isbn; // ✅ Required for setIsbn()
    private String description;

    private LocalDate publicationDate; // ✅ Required for setPublicationDate()
}
