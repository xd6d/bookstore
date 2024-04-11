package com.example.bookstore.dto;

import lombok.Data;

@Data
public class BookDto {
    private String title;
    private String author;
    private String isbn;
    private Integer quantity;
}
