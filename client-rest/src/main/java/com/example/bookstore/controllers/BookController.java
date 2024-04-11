package com.example.bookstore.controllers;

import com.example.bookstore.dto.Book;
import com.example.bookstore.dto.BookDto;
import com.example.bookstore.dto.BookMapper;
import com.example.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping
    public Book createBook(@RequestBody BookDto bookDto) {
        return bookService.create(bookMapper.toBook(bookDto));
    }

    @GetMapping
    public List<Book> getAllBooks(@RequestBody BookDto bookDto) {
        return bookService.getAllMatching(bookMapper.toBook(bookDto));
    }

    @PutMapping
    public void updateBook(@RequestBody Book book) {
        bookService.update(book);
    }

    @DeleteMapping
    public void deleteBook(@RequestBody Book book) {
        bookService.delete(book);
    }
}
