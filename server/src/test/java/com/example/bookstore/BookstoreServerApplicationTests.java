package com.example.bookstore;

import com.example.bookstore.config.TestContainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainerConfiguration.class)
class BookstoreServerApplicationTests {

    @Test
    void contextLoads() {
    }

}
