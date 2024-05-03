package org.example.bookstore.service.impl;

import java.util.List;
import lombok.AllArgsConstructor;
import org.example.bookstore.dto.BookDto;
import org.example.bookstore.dto.BookRequestDto;
import org.example.bookstore.exception.EntityNotFoundException;
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
    public BookDto save(BookRequestDto requestDto) {
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
    public BookDto updateBookById(Long id, BookRequestDto updatedBookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + id));
        if (updatedBookDto == null) {
            throw new EntityNotFoundException("please provide update details ");
        }
        existingBook.setTitle(updatedBookDto.getTitle());
        existingBook.setAuthor(updatedBookDto.getAuthor());
        existingBook.setDescription(updatedBookDto.getDescription());
        existingBook.setIsbn(updatedBookDto.getIsbn());
        existingBook.setPrice(updatedBookDto.getPrice());
        existingBook.setCoverImage(updatedBookDto.getCoverImage());
        return bookMapper.toDto(bookRepository.save(existingBook));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
