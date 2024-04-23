package org.example.bookstore.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.example.bookstore.dto.BookDto;
import org.example.bookstore.dto.CreateBookRequestDto;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.service.BookService;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        Optional<List<Book>> booksOptional = Optional.ofNullable(bookRepository.findAll());
        return booksOptional
                .map(books -> books.stream()
                        .map(bookMapper::toDto)
                        .toList())
                .orElse(Collections.emptyList());
    }

    @Override
    public BookDto getBookById(Long id) {
        Book bookById = bookRepository.getBookById(id);
        return bookMapper.toDto(bookById);
    }
}
