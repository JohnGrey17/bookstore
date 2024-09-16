package org.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.utils.BookTestDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {
        "classpath:database/book/add-books-to-books-table.sql",
        "classpath:database/categories/add-categories-to-categories-table.sql",
        "classpath:database/categories_books/add-categories-to-books.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/categories_books/delete-categories-books-table.sql",
        "classpath:database/book/remove-books-from-books-table.sql",
        "classpath:database/categories/remove-categories-from-books-table.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookRepositoryTest {

    private static final String NOT_CORRECT_ISBN = "978-3-16-148410111-1";

    private static final Integer TOTAL_LIST_OF_BOOKS_SIZE = 6;
    private static final Long CORRECT_BOOK_ID = 10L;
    private static final Long INCORRECT_BOOK_ID = 9L;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
            Find book by ISBN
            """)
    void findByIsbn_BookWithThatIsbn() {
        Optional<Book> actual = bookRepository.findByIsbn("978-3-16-148410-1");

        Book expected = BookTestDataFactory.createBook();

        assertTrue(actual.isPresent(), "Book should be present");

        Book actualBook = actual.get();

        assertEquals(expected.getTitle(), actualBook.getTitle(), "Book title should match");
        assertEquals(expected.getAuthor(), actualBook.getAuthor(), "Book author should match");
        assertEquals(expected.getIsbn(), actualBook.getIsbn(), "Book Isbn should match");
        assertEquals(0, expected.getPrice().compareTo(actualBook.getPrice()),
                "Book price should match");
    }

    @Test
    @DisplayName("Get negative result when book with signed Isbn is not correct")
    void findByIsbn_BookDoesNotExist_NegativeResult() {
        Optional<Book> missingBook = bookRepository.findByIsbn(NOT_CORRECT_ISBN);
        Assertions.assertFalse(missingBook.isPresent(), "Book should not be present");
    }

    @Test
    @DisplayName("Find all books with existing categories")
    void findAllWithCategories_whereCategoriesExist() {
        List<Book> actual = bookRepository.findAllWithCategories();
        assertEquals(2, actual.size(),
                "Result should match specified value");
    }

    @Test
    @DisplayName("Get negative result when books don`t have categories")
    void findAllWithCategories_negativeResult() {
        List<Book> actualAllWithCategories = bookRepository.findAllWithCategories();
        Assertions.assertNotEquals(TOTAL_LIST_OF_BOOKS_SIZE, actualAllWithCategories.size());
    }

    @Test
    @DisplayName("Find book by id")
    void findByIdWithCategories_PositiveResult() {
        Optional<Book> bookById = bookRepository.findByIdWithCategories(CORRECT_BOOK_ID);

        Book expected = BookTestDataFactory.createBook();

        assertTrue(bookById.isPresent(), "Book should be present");

        Book actualBook = bookById.get();

        assertEquals(expected.getTitle(), actualBook.getTitle(), "Book title should match");
        assertEquals(expected.getAuthor(), actualBook.getAuthor(), "Book author should match");
        assertEquals(expected.getIsbn(), actualBook.getIsbn(), "Book Isbn should match");
        assertEquals(0, expected.getPrice().compareTo(actualBook.getPrice()),
                "Book price should match");
    }

    @Test
    @DisplayName("Negative result with incorrect ID")
    void findByIdWithCategories_withWrongId_NegativeResult() {
        Optional<Book> result = bookRepository.findByIdWithCategories(INCORRECT_BOOK_ID);

        assertTrue(result.isEmpty(), "Book should not be found");
    }

    @Test
    @DisplayName("Negative result with incorrect ID")
    void findAllByCategoryId_NegativeResult() {
        Pageable pageable = Pageable.unpaged();
        List<Book> books = bookRepository.findAllByCategoryId(9L, pageable);

        assertEquals(0, books.size(),
                "The number of books should match the expected count");
    }
}
