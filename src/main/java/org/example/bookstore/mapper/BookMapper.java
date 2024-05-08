package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.bookdto.BookRegistrationRequestDto;
import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.example.bookstore.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookResponseDto toDto(Book book);

    Book toModel(BookRegistrationRequestDto bookRequestDto);
}
