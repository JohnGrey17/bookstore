package org.example.bookstore.service;

import java.util.List;
import org.example.bookstore.dto.BookDto;
import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.dto.BookSearchParameters;

public interface BookService {
    BookDto create(BookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    BookDto updateBookById(Long id, BookRequestDto updatedBook);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters parameters);
}
