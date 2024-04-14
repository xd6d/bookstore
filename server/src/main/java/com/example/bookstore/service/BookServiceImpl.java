package com.example.bookstore.service;

import com.example.bookstore.dao.BookRepository;
import com.example.bookstore.model.Book;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book create(Book book) {
        if (bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new EntityExistsException("Book with isbn %s already exists".formatted(book.getIsbn()));
        }
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllMatching(Book book) {
        return bookRepository.findAll(Example.of(book, getBookExampleMatcher()));
    }

    @Override
    public void update(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    private ExampleMatcher getBookExampleMatcher() {
        return ExampleMatcher.matching()
                .withIgnorePaths("id", "quantity")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnoreNullValues();
    }
}
