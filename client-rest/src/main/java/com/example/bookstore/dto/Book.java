package com.example.bookstore.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Book {
    private UUID id;
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
}