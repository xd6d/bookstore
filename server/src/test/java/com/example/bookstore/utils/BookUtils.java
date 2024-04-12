package com.example.bookstore.utils;

import com.example.bookstore.model.Book;

import java.util.UUID;

public class BookUtils {

    private static final Book DEFAULT_BOOK = new Book();

    static {
        DEFAULT_BOOK.setId(UUID.fromString("386c0818-8893-4c34-5678-e5a7b356f45c"));
        DEFAULT_BOOK.setAuthor("Agatha Christie");
        DEFAULT_BOOK.setTitle("Murder on the Orient Express");
        DEFAULT_BOOK.setIsbn("987654");
        DEFAULT_BOOK.setQuantity(40);
    }

    public static Book getDefaultBook() {
        return DEFAULT_BOOK;
    }
}
