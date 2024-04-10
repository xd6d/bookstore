package com.example.bookstore;

import com.example.bookstore.service.BookServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookstoreClientApplication implements CommandLineRunner {

    private static final Logger logger = LogManager.getLogger(BookstoreClientApplication.class);

    @GrpcClient("bookstore-client")
    BookServiceGrpc.BookServiceBlockingStub stub;

    public static void main(String[] args) {
        SpringApplication.run(BookstoreClientApplication.class, args);
    }

    @Override
    public void run(String... args) {
//        logger.info("\n" + stub.create(BookRequest.newBuilder().setAuthor("test1").setTitle("test2").setIsbn("222").setQuantity(6).build()));
//        stub.update(BookDto.newBuilder().setId("61384666-757e-484d-acd0-226d461bf1a5").setAuthor("Franz Kafka").setTitle("The Castle").setIsbn("345").setQuantity(111).build());
//        stub.delete(BookDto.newBuilder().setId("ac1fa072-b2e6-4304-8d06-9b6a9fc78269").build());
//        stub.getAll(BookRequest.newBuilder().setAuthor("kafka").build()).forEachRemaining(logger::info);
    }
}
