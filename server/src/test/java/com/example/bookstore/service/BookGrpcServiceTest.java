package com.example.bookstore.service;

import com.example.bookstore.config.TestContainerConfiguration;
import com.example.bookstore.dao.BookRepository;
import com.example.bookstore.grpc.BookGrpcService;
import com.example.bookstore.mappers.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.utils.BookUtils;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestContainerConfiguration.class)
class BookGrpcServiceTest {

    private static final Book EXAMPLE_BOOK = BookUtils.getDefaultBook();

    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Autowired
    private BookGrpcService bookGrpcService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @AfterEach
    void clearAll() {
        bookRepository.deleteAll();
    }

    @Test
    void bookCreateTest() {
        var response = bookGrpcService.create(Mono.just(bookMapper.toRequest(EXAMPLE_BOOK))).block(TIMEOUT);
        assertNotNull(response);
        assertDoesNotThrow(() -> UUID.fromString(response.getId()), "ID have not been created");

        var optionalSaved = bookRepository.findById(UUID.fromString(response.getId()));
        assertTrue(optionalSaved.isPresent(), "Book has not been created");

        var saved = optionalSaved.get();
        assertEquals(EXAMPLE_BOOK.getTitle(), saved.getTitle(), "Titles should match");
        assertEquals(EXAMPLE_BOOK.getAuthor(), saved.getAuthor(), "Authors should match");
        assertEquals(EXAMPLE_BOOK.getIsbn(), saved.getIsbn(), "ISBNs should match");
        assertEquals(EXAMPLE_BOOK.getQuantity(), saved.getQuantity(), "Quantities should match");
    }

    @Test
    @Sql("/book.sql")
    void getAllTest() {
        var response = bookGrpcService.getAll(bookMapper.toRequest(EXAMPLE_BOOK));

        assertEquals(1, response.count().block(TIMEOUT), "Could not find specified book");

        var found = response.blockFirst(TIMEOUT);
        assertNotNull(found, "Could not find specified book");
        assertEquals(EXAMPLE_BOOK.getId().toString(), found.getId(), "Ids should match");
        assertEquals(EXAMPLE_BOOK.getTitle(), found.getTitle(), "Titles should match");
        assertEquals(EXAMPLE_BOOK.getAuthor(), found.getAuthor(), "Authors should match");
        assertEquals(EXAMPLE_BOOK.getIsbn(), found.getIsbn(), "ISBNs should match");
        assertEquals(EXAMPLE_BOOK.getQuantity(), found.getQuantity(), "Quantities should match");

        response = bookGrpcService.getAll(BookRequest.getDefaultInstance());
        assertEquals(bookRepository.findAll().size(), response.count().block(TIMEOUT), "Entity amounts should match");
    }
}