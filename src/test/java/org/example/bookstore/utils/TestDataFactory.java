package org.example.bookstore.utils;

import java.math.BigDecimal;
import org.example.bookstore.model.Book;

public class TestDataFactory {

    private static final String BOOK_TITLE = "Test Book_1";
    private static final String BOOK_AUTHOR = "Test Author_1";
    private static final String BOOK_ISBN = "978-3-16-148410-1";
    private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(100.0);

    public static Book createBook() {
        Book book = new Book();
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setIsbn(BOOK_ISBN);
        book.setPrice(BOOK_PRICE);
        return book;
    }

}
