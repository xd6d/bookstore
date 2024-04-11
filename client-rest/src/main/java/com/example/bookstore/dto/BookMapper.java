package com.example.bookstore.dto;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {
    Book toBook(BookDto bookDto);
}
