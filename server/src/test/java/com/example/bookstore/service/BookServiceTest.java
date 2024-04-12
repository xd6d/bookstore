package com.example.bookstore.service;

import com.example.bookstore.config.TestContainerConfiguration;
import com.example.bookstore.dao.BookRepository;
import com.example.bookstore.mappers.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.utils.BookUtils;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

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
    void bookCreateTest() throws IOException {
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(bookService).build().start());

        BookServiceGrpc.BookServiceBlockingStub blockingStub = BookServiceGrpc.newBlockingStub(
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()));

        var response = blockingStub.create(bookMapper.toRequest(EXAMPLE_BOOK));
        assertDoesNotThrow(() -> UUID.fromString(response.getId()), "ID have not been created");
        assertTrue(bookRepository.findById(UUID.fromString(response.getId())).isPresent(), "Book has not been created");
    }

    @Test
    @Sql("/book.sql")
    void getAllTest() throws IOException {
        String serverName = InProcessServerBuilder.generateName();
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(bookService).build().start());

        BookServiceGrpc.BookServiceBlockingStub blockingStub = BookServiceGrpc.newBlockingStub(
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()));

        var response = blockingStub.getAll(bookMapper.toRequest(EXAMPLE_BOOK));
        var responseList = toList(response);
        assertEquals(1, responseList.size(), "Could not find specified book");
        var found = bookMapper.toEntity(responseList.get(0));
        assertEquals(EXAMPLE_BOOK.getTitle(), found.getTitle(), "Titles should match");
        assertEquals(EXAMPLE_BOOK.getAuthor(), found.getAuthor(), "Authors should match");
        assertEquals(EXAMPLE_BOOK.getIsbn(), found.getIsbn(), "ISBNs should match");
        assertEquals(EXAMPLE_BOOK.getQuantity(), found.getQuantity(), "Quantities should match");
        assertEquals(EXAMPLE_BOOK, found, "Books should match");
        responseList = toList(blockingStub.getAll(BookRequest.getDefaultInstance()));
        assertEquals(bookRepository.findAll().size(), responseList.size(), "Entity amounts should match");
    }

    private <T> List<T> toList(Iterator<T> iterator) {
        List<T> list = new LinkedList<>();
        iterator.forEachRemaining(list::add);
        return list;
    }
}