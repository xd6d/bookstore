package com.example.bookstore.service;

import com.example.bookstore.config.TestContainerConfiguration;
import com.example.bookstore.dao.BookRepository;
import com.example.bookstore.mappers.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.utils.BookUtils;
import io.grpc.internal.testing.StreamRecorder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestContainerConfiguration.class)
class BookServiceTest {

    private static final Book EXAMPLE_BOOK = BookUtils.getDefaultBook();

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @AfterEach
    void clearAll() {
        bookRepository.deleteAll();
    }

    @Test
    void bookCreateTest() throws Exception {
        StreamRecorder<BookDto> responseObserver = StreamRecorder.create();
        bookService.create(bookMapper.toRequest(EXAMPLE_BOOK), responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }
        assertNull(responseObserver.getError());

        List<BookDto> results = responseObserver.getValues();
        assertEquals(1, results.size());

        var response = results.get(0);

        assertDoesNotThrow(() -> UUID.fromString(response.getId()), "ID have not been created");
        assertTrue(bookRepository.findById(UUID.fromString(response.getId())).isPresent(), "Book has not been created");
    }

    @Test
    @Sql("/book.sql")
    void getAllTest() throws Exception {
        StreamRecorder<BookDto> responseObserver = StreamRecorder.create();

        bookService.getAll(bookMapper.toRequest(EXAMPLE_BOOK), responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }
        assertNull(responseObserver.getError());

        List<BookDto> results = responseObserver.getValues();
        assertEquals(1, results.size(), "Could not find specified book");


        var found = bookMapper.toEntity(results.get(0));
        assertEquals(EXAMPLE_BOOK.getTitle(), found.getTitle(), "Titles should match");
        assertEquals(EXAMPLE_BOOK.getAuthor(), found.getAuthor(), "Authors should match");
        assertEquals(EXAMPLE_BOOK.getIsbn(), found.getIsbn(), "ISBNs should match");
        assertEquals(EXAMPLE_BOOK.getQuantity(), found.getQuantity(), "Quantities should match");
        assertEquals(EXAMPLE_BOOK, found, "Books should match");

        responseObserver = StreamRecorder.create();
        bookService.getAll(BookRequest.getDefaultInstance(), responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }
        assertNull(responseObserver.getError());

        results = responseObserver.getValues();
        assertEquals(bookRepository.findAll().size(), results.size(), "Entity amounts should match");
    }
}