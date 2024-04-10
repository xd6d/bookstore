package com.example.bookstore.mappers;

import com.example.bookstore.model.Book;
import com.example.bookstore.service.BookDto;
import com.example.bookstore.service.BookRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toEntity(BookRequest bookRequest);

    Book toEntity(BookDto bookDto);

    BookDto toResponse(Book user);
}
