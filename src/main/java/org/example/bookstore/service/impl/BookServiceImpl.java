package org.example.bookstore.service.impl;

import lombok.AllArgsConstructor;
import org.example.bookstore.dto.BookDto;
import org.example.bookstore.dto.CreateBookRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id).stream()
                .map(bookMapper::toDto)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "can`t find book with id: " + id));
    }

    @Override
    public Book updateBookById(Long id, BookDto updatedBookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + id));
        if (updatedBookDto.getTitle() != null && !updatedBookDto.getTitle().isEmpty()) {
            existingBook.setTitle(updatedBookDto.getTitle());
        }
        if (updatedBookDto.getAuthor() != null && !updatedBookDto.getAuthor().isEmpty()) {
            existingBook.setAuthor(updatedBookDto.getAuthor());
        }
        if (updatedBookDto.getDescription() != null && !updatedBookDto.getDescription().isEmpty()) {
            existingBook.setDescription(updatedBookDto.getDescription());
        }
        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
