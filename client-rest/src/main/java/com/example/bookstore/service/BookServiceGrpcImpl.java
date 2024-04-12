package com.example.bookstore.service;

import com.example.bookstore.dto.Book;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class BookServiceGrpcImpl implements BookService {

    @GrpcClient("bookstore-client")
    private BookServiceGrpc.BookServiceBlockingStub stub;

    @Override
    public Book create(Book book) {
        return toBook(stub.create(toBookRequest(book)));
    }

    @Override
    public List<Book> getAllMatching(Book book) {
        List<Book> books = new LinkedList<>();
        stub.getAll(toBookRequest(book)).forEachRemaining(b -> books.add(toBook(b)));
        return books;
    }

    @Override
    public void update(Book book) {
        stub.update(toBookDto(book));
    }

    @Override
    public void delete(Book book) {
        stub.delete(toBookDto(book));
    }

    private BookRequest toBookRequest(Book book) {
        return BookRequest.newBuilder()
                .setAuthor(book.getAuthor())
                .setTitle(book.getTitle())
                .setIsbn(book.getIsbn())
                .setQuantity(book.getQuantity())
                .build();
    }

    private BookDto toBookDto(Book book) {
        return BookDto.newBuilder()
                .setId(book.getId().toString())
                .setAuthor(book.getAuthor())
                .setTitle(book.getTitle())
                .setIsbn(book.getIsbn())
                .setQuantity(book.getQuantity())
                .build();
    }

    private Book toBook(BookDto bookDto) {
        return Book.builder()
                .id(UUID.fromString(bookDto.getId()))
                .author(bookDto.getAuthor())
                .title(bookDto.getTitle())
                .isbn(bookDto.getIsbn())
                .quantity(bookDto.getQuantity())
                .build();
    }
}
