package org.example.bookstore.service;

import java.util.List;
import org.example.bookstore.dto.BookDto;
import org.example.bookstore.dto.CreateBookRequestDto;
import org.example.bookstore.model.Book;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    Book updateBookById(Long id, BookDto updatedBook);

    void deleteById(Long id);
}
