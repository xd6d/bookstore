package com.example.bookstore.service;

import com.example.bookstore.config.TestContainerConfiguration;
import com.example.bookstore.dao.BookRepository;
import com.example.bookstore.mappers.BookMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.utils.BookUtils;
import jakarta.persistence.EntityNotFoundException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.citrusframework.TestActionRunner;
import org.citrusframework.annotations.CitrusResource;
import org.citrusframework.annotations.CitrusTest;
import org.citrusframework.junit.jupiter.CitrusSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;

import static org.citrusframework.actions.CreateVariablesAction.Builder.createVariable;
import static org.citrusframework.actions.ExecuteSQLAction.Builder.sql;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TestContainerConfiguration.class)
@CitrusSupport
public class BookServiceIT {

    private static final Book EXAMPLE_BOOK = BookUtils.getDefaultBook();

    @Autowired
    private DataSource dataSource;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapper bookMapper;

    @GrpcClient("bookstore-client")
    private BookServiceGrpc.BookServiceBlockingStub stub;

    @AfterEach
    void clearAll() {
        bookRepository.deleteAll();
    }

    @Test
    @Sql("/book.sql")
    @CitrusTest
    void updateBookTest(@CitrusResource TestActionRunner actions) {
        actions.run(createVariable("bookId", EXAMPLE_BOOK.getId().toString()));
        actions.run(createVariable("author", EXAMPLE_BOOK.getAuthor()));
        final String NEW_TITLE = "Harry Potter";

        BookDto request = bookMapper.toDto(EXAMPLE_BOOK);
        request = request.toBuilder().setTitle(NEW_TITLE).build();
        stub.update(request);

        actions.$(sql()
                .dataSource(dataSource)
                .query()
                .statement("SELECT author FROM books WHERE id = '${bookId}'")
                .statement("SELECT title FROM books WHERE id = '${bookId}'")
                .validate("author", "${author}")
                .validate("title", NEW_TITLE));

        Book found = bookRepository.findById(EXAMPLE_BOOK.getId()).orElseThrow(
                () -> new EntityNotFoundException("Book not found")
        );
        assertNotEquals(found.getTitle(), EXAMPLE_BOOK.getTitle(), "Title should have been changed");
        assertEquals(found.getTitle(), NEW_TITLE, "Title have not been changed");
    }

    @Test
    @Sql("/book.sql")
    @CitrusTest
    void deleteBookTest(@CitrusResource TestActionRunner actions) {
        actions.run(createVariable("bookId", EXAMPLE_BOOK.getId().toString()));

        actions.$(sql()
                .dataSource(dataSource)
                .query()
                .statement("SELECT COUNT(*) as books_amount FROM books WHERE id = '${bookId}'")
                .validate("books_amount", "1"));

        stub.delete(bookMapper.toDto(EXAMPLE_BOOK));

        actions.$(sql()
                .dataSource(dataSource)
                .query()
                .statement("SELECT COUNT(*) as books_amount FROM books WHERE id = '${bookId}'")
                .validate("books_amount", "0"));

        var found = bookRepository.findById(EXAMPLE_BOOK.getId());
        assertTrue(found.isEmpty(), "Book should not been found");
    }
}
