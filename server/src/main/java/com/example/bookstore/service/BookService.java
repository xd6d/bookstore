package com.example.bookstore.service;

import com.example.bookstore.dao.BookRepository;
import com.example.bookstore.mappers.BookMapper;
import com.example.bookstore.model.Book;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

@GrpcService
@RequiredArgsConstructor
public class BookService extends BookServiceGrpc.BookServiceImplBase {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public void create(BookRequest request, StreamObserver<BookDto> responseObserver) {
        Book created = bookRepository.save(bookMapper.toEntity(request));
        responseObserver.onNext(bookMapper.toResponse(created));
        responseObserver.onCompleted();
    }

    @Override
    public void getAll(BookRequest request, StreamObserver<BookDto> responseObserver) {
        bookRepository.findAll(Example.of(bookMapper.toEntity(request), getBookExampleMatcher()))
                .stream()
                .map(bookMapper::toResponse)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void update(BookDto request, StreamObserver<Empty> responseObserver) {
        bookRepository.save(bookMapper.toEntity(request));
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void delete(BookDto request, StreamObserver<Empty> responseObserver) {
        bookRepository.delete(bookMapper.toEntity(request));
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    private ExampleMatcher getBookExampleMatcher() {
        return ExampleMatcher.matching()
                .withIgnorePaths("id", "quantity")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnoreNullValues();
    }
}
