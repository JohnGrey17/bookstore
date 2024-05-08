package org.example.bookstore.service.book;

import java.util.List;
import org.example.bookstore.dto.bookdto.BookRegistrationRequestDto;
import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.example.bookstore.dto.bookdto.BookSearchParameters;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto create(BookRegistrationRequestDto requestDto);

    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto getBookById(Long id);

    BookResponseDto updateBookById(Long id, BookRegistrationRequestDto updatedBook);

    void deleteById(Long id);

    List<BookResponseDto> search(BookSearchParameters parameters, Pageable pageable);
}
