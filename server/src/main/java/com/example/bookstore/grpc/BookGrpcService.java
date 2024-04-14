package com.example.bookstore.grpc;

import com.example.bookstore.mappers.BookMapper;
import com.example.bookstore.service.BookDto;
import com.example.bookstore.service.BookRequest;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.ReactorBookServiceGrpc;
import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@GrpcService
@RequiredArgsConstructor
public class BookGrpcService extends ReactorBookServiceGrpc.BookServiceImplBase {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private final BookService bookService;
    private final BookMapper bookMapper;

    @Override
    public Mono<BookDto> create(Mono<BookRequest> request) {
        return request.map(req -> bookService.create(bookMapper.toEntity(req)))
                .map(bookMapper::toDto)
                .timeout(TIMEOUT);
    }

    @Override
    public Flux<BookDto> getAll(Mono<BookRequest> request) {
        return request.map(bookMapper::toEntity)
                .flatMapIterable(bookService::getAllMatching)
                .map(bookMapper::toDto)
                .timeout(TIMEOUT);
    }

    @Override
    public Mono<Empty> update(Mono<BookDto> request) {
        return request.doOnNext(req -> bookService.update(bookMapper.toEntity(req)))
                .thenReturn(Empty.getDefaultInstance());
    }

    @Override
    public Mono<Empty> delete(Mono<BookDto> request) {
        return request.doOnNext(req -> bookService.delete(bookMapper.toEntity(req)))
                .thenReturn(Empty.getDefaultInstance());
    }
}
