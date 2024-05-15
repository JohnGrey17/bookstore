package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.bookdto.BookRequestDto;
import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.example.bookstore.dto.bookdto.BookSearchParameters;
import org.example.bookstore.service.book.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/books")
@Tag(name = "Book Controller", description = "This controller handles requests "
        + "and responses related to books in the database")
public class BookControllerImpl {

    private final BookService bookService;

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get all books", description = "Get all existing books from DB")
    public List<BookResponseDto> getAllBooks(Pageable pageable) {
        return bookService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Get book by id", description = "Get existing book by id")
    public BookResponseDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create book", description = "Create book with parameters in DB")
    public BookResponseDto createBook(@RequestBody @Valid BookRequestDto requestDto) {
        return bookService.create(requestDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update book by id", description = "Update existing book "
            + "by id with valid parameters")
    public BookResponseDto updateBookById(@PathVariable Long id,
                                          @Valid @RequestBody BookRequestDto book) {
        return bookService.updateBookById(id, book);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Operation(summary = "Search books", description = "Search for books based"
            + " on specified parameters.")
    public List<BookResponseDto> search(BookSearchParameters searchParameters, Pageable pageable) {
        return bookService.search(searchParameters, pageable);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete book", description = "Mark book as deleted from DB "
            + "and user won`t see information about it")
    public void delete(@PathVariable Long id) {
        bookService.deleteById(id);
    }
}
