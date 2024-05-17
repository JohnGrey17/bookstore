package org.example.bookstore.mapper;

import java.util.stream.Collectors;
import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.bookdto.BookRequestDto;
import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.example.bookstore.dto.bookdto.BookResponseWithoutCategory;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookResponseDto toDto(Book book);

    Book toModel(BookRequestDto bookRequestDto);

    BookResponseWithoutCategory toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookRequestDto requestDto, Book book) {
        book.getCategories().stream().map(Category::getId).collect(Collectors.toSet());
    }
}


