package org.example.bookstore.service.book;

import jakarta.transaction.Transactional;
import java.util.HashSet;
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

        Set<Long> categoryIds = new HashSet<>(requestDto.getCategoryIds());
        List<Category> categories = categoryRepository.findCategoriesByIds(categoryIds);

        if (categories.size() != categoryIds.size()) {
            Set<Long> missingCategoryIds = findMissingCategoryIds(categoryIds, categories);
            throw new CategoryException("Categories not found with ids: " + missingCategoryIds);
        }

        book.setCategories(new HashSet<>(categories));
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookResponseDto> findAll(Pageable pageable) {
        return bookRepository.findAllWithCategories().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookResponseDto getBookById(Long id) {
        return bookRepository.findByIdWithCategories(id).stream()
                .map(bookMapper::toDto)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "can`t find book with id: " + id));
    }

    @Override
    @Transactional
    public BookResponseDto updateBookById(Long id, BookRequestDto updatedBookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + id));

        String isbn = updatedBookDto.getIsbn();
        if (isbn != null && !isbn.equals(existingBook.getIsbn()) && bookRepository
                .findByIsbn(isbn).isPresent()) {
            throw new EntityNotFoundException("Book with ISBN " + isbn + " already exists.");
        }

        Set<Long> categoryIds = new HashSet<>(updatedBookDto.getCategoryIds());
        List<Category> categories = categoryRepository.findCategoriesByIds(categoryIds);

        if (categories.size() != categoryIds.size()) {
            Set<Long> missingCategoryIds = findMissingCategoryIds(categoryIds, categories);
            throw new CategoryException("Categories not found with ids: " + missingCategoryIds);
        }

        existingBook.setTitle(updatedBookDto.getTitle());
        existingBook.setAuthor(updatedBookDto.getAuthor());
        existingBook.setDescription(updatedBookDto.getDescription());
        existingBook.setIsbn(updatedBookDto.getIsbn());
        existingBook.setPrice(updatedBookDto.getPrice());
        existingBook.setCoverImage(updatedBookDto.getCoverImage());
        existingBook.setCategories(new HashSet<>(categories));

        return bookMapper.toDto(bookRepository.save(existingBook));
    }

    @Override
    @Transactional()
    public List<BookResponseDto> search(BookSearchParameters parameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(parameters);
        return bookRepository.findAll(bookSpecification).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + id));
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId, Pageable pageable) {
        return bookRepository.findAllByCategoryId(categoryId, pageable).stream()
                .map(bookMapper::toDtoWithoutCategoryIds)
                .collect(Collectors.toList());
    }

    private Set<Long> findMissingCategoryIds(Set<Long> categoryIds, List<Category> categories) {
        Set<Long> foundCategoryIds = categories.stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        categoryIds.removeAll(foundCategoryIds);
        return categoryIds;
    }
}
