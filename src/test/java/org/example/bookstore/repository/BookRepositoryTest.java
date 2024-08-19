package org.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.example.bookstore.model.Book;
import org.example.bookstore.repository.book.BookRepository;
import org.example.bookstore.utils.TestDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    private static final String NOT_CORRECT_ISBN = "978-3-16-148410111-1";
    private static final Integer LIST_OF_BOOKS_WITH_CATEGORIES_SIZE = 5;
    private static final Integer TOTAL_LIST_OF_BOOKS_SIZE = 6;
    private static final Long CORRECT_BOOK_ID = 10L;
    private static final Long INCORRECT_BOOK_ID = 9L;
    private static final Long CATEGORY_ID = 11L;
    private static final int EXPECTED_BOOK_COUNT = 2;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
            Find book by ISBN
            """)
    @Sql(scripts = {
            "classpath:database/book/add-books-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIsbn_BookWithThatIsbn() {
        Optional<Book> actual = bookRepository.findByIsbn("978-3-16-148410-1");

        Book expected = TestDataFactory.createBook();

        assertTrue(actual.isPresent(), "Book should be present");

        Book actualBook = actual.get();

        assertEquals(expected.getTitle(), actualBook.getTitle(), "Book title should match");
        assertEquals(expected.getAuthor(), actualBook.getAuthor(), "Book author should match");
        assertEquals(expected.getIsbn(), actualBook.getIsbn(), "Book Isbn should match");
        assertEquals(0, expected.getPrice().compareTo(actualBook.getPrice()),
                "Book price should match");
    }

    @Test
    @DisplayName("Find book by ISBN when book does not exist")
    @Sql(scripts = {
            "classpath:database/book/add-books-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/book/remove-books-from-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findByIsbn_BookDoesNotExist() {
        Optional<Book> missingBook = bookRepository.findByIsbn(NOT_CORRECT_ISBN);
        Assertions.assertFalse(missingBook.isPresent(), "Book should not be present");
    }

    @Test
    @DisplayName("Find all books with existing categories")
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
    void findAllWithCategories_whereCategoriesExist() {
        List<Book> actual = bookRepository.findAllWithCategories();
        assertEquals(LIST_OF_BOOKS_WITH_CATEGORIES_SIZE,actual.size(),
                "Result should match specified value");
    }

    @Test
    @DisplayName("Get negative result when books don`t have categories")
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
    void findAllWithCategories_negativeResult() {
        List<Book> actualAllWithCategories = bookRepository.findAllWithCategories();
        Assertions.assertNotEquals(TOTAL_LIST_OF_BOOKS_SIZE, actualAllWithCategories.size());
    }

    @Test
    @DisplayName("Find book by id")
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
    void findByIdWithCategories() {
        Optional<Book> bookById = bookRepository.findByIdWithCategories(CORRECT_BOOK_ID);

        Book expected = TestDataFactory.createBook();

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
    void findByIdWithCategories_withWrongId() {
        Optional<Book> result = bookRepository.findByIdWithCategories(INCORRECT_BOOK_ID);

        assertTrue(result.isEmpty(), "Book should not be found");
    }
    @Test
    @DisplayName("Negative result with incorrect ID")
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
    void findAllByCategoryId() {
        Pageable pageable = Pageable.unpaged();
        List<Book> books = bookRepository.findAllByCategoryId(CATEGORY_ID, pageable);

        assertEquals(EXPECTED_BOOK_COUNT, books.size(),
                "The number of books should match the expected count");
    }
}
