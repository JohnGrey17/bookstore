package org.example.bookstore.service.book;

import java.util.List;
import org.example.bookstore.dto.bookdto.BookRequestDto;
import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.example.bookstore.dto.bookdto.BookSearchParameters;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookResponseDto create(BookRequestDto requestDto);

    List<BookResponseDto> findAll(Pageable pageable);

    BookResponseDto getBookById(Long id);

    BookResponseDto updateBookById(Long id, BookRequestDto updatedBook);

    void deleteById(Long id);

    List<BookResponseDto> search(BookSearchParameters parameters, Pageable pageable);

    List<BookResponseDto> getBookDtoByCategoryId(Long id, Pageable pageable);
}
