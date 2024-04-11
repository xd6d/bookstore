package com.example.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestBookstoreApplication {

    public static void main(String[] args) {
        SpringApplication.from(BookstoreClientApplication::main).with(TestBookstoreApplication.class).run(args);
    }

}
