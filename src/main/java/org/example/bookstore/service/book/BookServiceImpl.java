package org.example.bookstore.service.book;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.example.bookstore.dto.bookdto.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.bookdto.BookRequestDto;
import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.example.bookstore.dto.bookdto.BookSearchParameters;
import org.example.bookstore.exception.CategoryException;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.BookMapper;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.repository.book.BookSpecificationBuilder;
import org.example.bookstore.repository.category.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public BookResponseDto create(BookRequestDto requestDto) {
        String isbn = requestDto.getIsbn();
        if (isbn != null && bookRepository.findByIsbn(isbn).isPresent()) {
            throw new EntityNotFoundException("Book with ISBN " + isbn + " already exists.");
        }

        Book book = bookMapper.toModel(requestDto);

        Set<Category> categories = requestDto.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryException(
                                "Category not found with id: " + categoryId)))
                .collect(Collectors.toSet());
        book.setCategories(categories);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookResponseDto getBookById(Long id) {
        return bookRepository.findById(id).stream()
                .map(bookMapper::toDto)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "can`t find book with id: " + id));
    }

    @Override
    public BookResponseDto updateBookById(Long id, BookRequestDto updatedBookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + id));
        String isbn = updatedBookDto.getIsbn();
        if (isbn != null && bookRepository.findByIsbn(isbn).isPresent()) {
            throw new EntityNotFoundException("Book with ISBN " + isbn + " already exists.");
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
    public List<BookResponseDto> search(BookSearchParameters parameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(parameters);
        return bookRepository.findAll(bookSpecification).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + id));
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId, pageable).stream()
                .map(bookMapper::toDtoWithoutCategoryIds)
                .collect(Collectors.toList());
    }
}
