package com.example.bookstore.service;

import com.example.bookstore.dto.Book;

import java.util.List;

public interface BookService {
    Book create(Book book);

    List<Book> getAllMatching(Book book);

    void update(Book book);

    void delete(Book book);
}
