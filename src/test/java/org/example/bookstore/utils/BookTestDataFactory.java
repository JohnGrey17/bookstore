package org.example.bookstore.utils;

import java.math.BigDecimal;
import java.util.Set;
import org.example.bookstore.dto.bookdto.BookDtoWithoutCategoryIds;
import org.example.bookstore.dto.bookdto.BookRequestDto;
import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.example.bookstore.model.Book;

public class BookTestDataFactory {

    private static final String BOOK_TITLE = "Test Book_1";
    private static final String BOOK_AUTHOR = "Test Author_1";
    private static final String BOOK_ISBN = "978-3-16-148410-1";
    private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(100.0);
    private static final BigDecimal BOOK_PRICE_FOR_CONTROLLER_TEST = BigDecimal.valueOf(100.0);
    private static final Long BOOK_ID = 1L;
    private static final String BOOK_DESCRIPTION = "Test Description";
    private static final Set<Long> CATEGORIES_ID = Set.of(10L,11L);
    private static final String UPDATED_BOOK_TITLE = "Updated Title";
    private static final String UPDATED_BOOK_AUTHOR = "Updated Author";
    private static final String UPDATED_BOOK_ISBN = "978-3-16-148410-0";
    private static final BigDecimal UPDATED_BOOK_PRICE = BigDecimal.valueOf(150.0);
    private static final String UPDATED_BOOK_DESCRIPTION = "Updated description";
    private static final String UPDATED_BOOK_COVER_IMAGE = "https://example.com/cover.jpg";
    private static final Set<Long> UPDATED_CATEGORIES_ID = Set.of(10L, 11L);
    private static final Long UPDATED_BOOK_ID = 10L;

    public static Book createBook() {
        Book book = new Book();
        book.setId(BOOK_ID);
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setDescription(BOOK_DESCRIPTION);
        book.setIsbn(BOOK_ISBN);
        book.setPrice(BOOK_PRICE);
        return book;
    }

    public static BookResponseDto createResponseDtoForController() {
        BookResponseDto bookResponseDto = new BookResponseDto();
        bookResponseDto.setId(10);
        bookResponseDto.setTitle(BOOK_TITLE);
        bookResponseDto.setAuthor(BOOK_AUTHOR);
        bookResponseDto.setIsbn(BOOK_ISBN);
        bookResponseDto.setPrice(BOOK_PRICE_FOR_CONTROLLER_TEST);
        bookResponseDto.setCategoryIds(CATEGORIES_ID);
        return bookResponseDto;
    }

    public static BookRequestDto createBookRequestDto() {
        BookRequestDto bookRequestDto = new BookRequestDto();
        bookRequestDto.setTitle(BOOK_TITLE);
        bookRequestDto.setAuthor(BOOK_AUTHOR);
        bookRequestDto.setIsbn(BOOK_ISBN);
        bookRequestDto.setPrice(BOOK_PRICE);
        return bookRequestDto;
    }

    public static BookResponseDto createBookResponseDto() {
        BookResponseDto bookResponseDto = new BookResponseDto();
        bookResponseDto.setId(BOOK_ID);
        bookResponseDto.setTitle(BOOK_TITLE);
        bookResponseDto.setAuthor(BOOK_AUTHOR);
        bookResponseDto.setIsbn(BOOK_ISBN);
        bookResponseDto.setPrice(BOOK_PRICE);
        return bookResponseDto;

    }

    public static BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds() {
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setId(BOOK_ID);
        bookDtoWithoutCategoryIds.setTitle(BOOK_TITLE);
        bookDtoWithoutCategoryIds.setAuthor(BOOK_AUTHOR);
        bookDtoWithoutCategoryIds.setIsbn(BOOK_ISBN);
        bookDtoWithoutCategoryIds.setPrice(BOOK_PRICE);
        return bookDtoWithoutCategoryIds;
    }

    public static BookRequestDto createUpdatedBookRequestDto() {
        BookRequestDto bookRequestDto = new BookRequestDto();
        bookRequestDto.setTitle(UPDATED_BOOK_TITLE);
        bookRequestDto.setAuthor(UPDATED_BOOK_AUTHOR);
        bookRequestDto.setIsbn(UPDATED_BOOK_ISBN);
        bookRequestDto.setPrice(UPDATED_BOOK_PRICE);
        bookRequestDto.setDescription(UPDATED_BOOK_DESCRIPTION);
        bookRequestDto.setCoverImage(UPDATED_BOOK_COVER_IMAGE);
        bookRequestDto.setCategoryIds(UPDATED_CATEGORIES_ID);
        return bookRequestDto;
    }

    public static BookResponseDto createUpdatedBookResponseDto() {
        BookResponseDto expectedResponseDto = new BookResponseDto();
        expectedResponseDto.setId(UPDATED_BOOK_ID);
        expectedResponseDto.setTitle(UPDATED_BOOK_TITLE);
        expectedResponseDto.setAuthor(UPDATED_BOOK_AUTHOR);
        expectedResponseDto.setIsbn(UPDATED_BOOK_ISBN);
        expectedResponseDto.setPrice(UPDATED_BOOK_PRICE);
        expectedResponseDto.setDescription(UPDATED_BOOK_DESCRIPTION);
        expectedResponseDto.setCoverImage(UPDATED_BOOK_COVER_IMAGE);
        expectedResponseDto.setCategoryIds(UPDATED_CATEGORIES_ID);
        return expectedResponseDto;
    }

}
