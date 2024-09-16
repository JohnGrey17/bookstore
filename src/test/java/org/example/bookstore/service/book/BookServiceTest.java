package org.example.bookstore.service.book;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
import org.example.bookstore.utils.BookTestDataFactory;
import org.example.bookstore.utils.CategoryTestDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    private static final String EXISTING_ISBN = "978-3-16-148410-1";
    private static final int EXPECTED_BOOK_LIST_SIZE = 1;

    private static final Long EXISTING_BOOK_ID = 1L;
    private static final Long NOT_EXISTING_ID = -1L;
    private static final Long CATEGORY_ID = 1L;

    private static final String DRAMA_NAME_OF_BOOK = "Drama";
    private static final String ACTION_NAME_OF_BOOK = "Action";
    private static final String TOLKIEN_AUTHOR_OF_BOOK = "Tolkien";
    private static final String LOVECRAFT_AUTHOR_OF_BOOK = "LoveCraft";

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Create new book with valid data should succeed")
    void create_WithCorrectProperties_PositiveResult() {
        // Given
        Category testCategoryDrama = CategoryTestDataFactory.createNewCategory_Drama();
        Category newCategoryAction = CategoryTestDataFactory.createNewCategory_Action();
        List<Category> testCategoryList = List.of(testCategoryDrama, newCategoryAction);

        BookRequestDto testBookRequestDto = BookTestDataFactory.createBookRequestDto();
        testBookRequestDto.setCategoryIds(Set.of(testCategoryDrama.getId(),
                newCategoryAction.getId()));

        Book testBook = BookTestDataFactory.createBook();
        BookResponseDto testResponseDto = BookTestDataFactory.createBookResponseDto();

        when(bookMapper.toModel(testBookRequestDto)).thenReturn(testBook);

        Set<Long> categoryIdsFromDto = new HashSet<>(testBookRequestDto.getCategoryIds());
        when(categoryRepository.findCategoriesByIds(categoryIdsFromDto))
                .thenReturn(testCategoryList);

        when(bookRepository.save(testBook)).thenReturn(testBook);
        when(bookMapper.toDto(testBook)).thenReturn(testResponseDto);

        // When
        BookResponseDto result = bookService.create(testBookRequestDto);

        // Then
        Assertions.assertEquals(testResponseDto, result);
        Assertions.assertNotNull(result);

        verify(bookMapper).toModel(testBookRequestDto);
        verify(categoryRepository).findCategoriesByIds(categoryIdsFromDto);
        verify(bookRepository).save(testBook);
        verify(bookMapper).toDto(testBook);
    }

    @Test
    @DisplayName("Create book with existing ISBN should throw EntityNotFoundException")
    void createWithExistingIsbn_negativeResult() {
        // Given
        BookRequestDto testRequestDto = BookTestDataFactory.createBookRequestDto();
        String existingIsbn = testRequestDto.getIsbn();
        Book existingBook = BookTestDataFactory.createBook();

        when(bookRepository.findByIsbn(existingIsbn)).thenReturn(Optional.of(existingBook));

        // When
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.create(testRequestDto));

        // Then
        Assertions.assertEquals("Book with ISBN " + existingIsbn
                + " already exists.", exception.getMessage());
    }

    @Test
    @DisplayName("Create book with missing categories should throw CategoryException")
    void createWithMissingCategories_negativeResult() {
        // Given
        BookRequestDto testRequestDto = BookTestDataFactory.createBookRequestDto();
        testRequestDto.setCategoryIds(Set.of(1L, 2L));

        Book testBook = BookTestDataFactory.createBook();

        List<Category> foundCategories = List.of(CategoryTestDataFactory.createNewCategory_Drama());
        when(bookRepository.findByIsbn(testRequestDto.getIsbn())).thenReturn(Optional.empty());
        when(bookMapper.toModel(testRequestDto)).thenReturn(testBook);
        when(categoryRepository.findCategoriesByIds(testRequestDto.getCategoryIds()))
                .thenReturn(foundCategories);

        // When
        CategoryException exception = Assertions.assertThrows(CategoryException.class,
                () -> bookService.create(testRequestDto));

        // Then
        Set<Long> missingCategoryIds = new HashSet<>(testRequestDto.getCategoryIds());
        missingCategoryIds.removeAll(foundCategories.stream().map(
                Category::getId).collect(Collectors.toSet()));
        Assertions.assertEquals("Categories not found with ids: "
                + missingCategoryIds, exception.getMessage());
    }

    @Test
    @DisplayName("Find all books should return a list of books")
    void findAll_Books_PositiveResult() {
        //Given
        Pageable pageable = PageRequest.of(0, 10);
        Book testBook = BookTestDataFactory.createBook();
        BookResponseDto testResponseDto = BookTestDataFactory.createBookResponseDto();
        List<Book> books = List.of(testBook);

        when(bookRepository.findAllWithCategories()).thenReturn(books);
        when(bookMapper.toDto(testBook)).thenReturn(testResponseDto);

        //When
        List<BookResponseDto> result = bookService.findAll(pageable);

        //Then
        Assertions.assertEquals(EXPECTED_BOOK_LIST_SIZE, result.size());
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("Get book by ID should return the correct book response")
    void getBookById_WithPositiveResult() {
        //Given
        Book testBook = BookTestDataFactory.createBook();
        BookResponseDto testResponseDto = BookTestDataFactory.createBookResponseDto();

        when(bookRepository.findByIdWithCategories(EXISTING_BOOK_ID))
                .thenReturn(Optional.of(testBook));
        when(bookMapper.toDto(testBook)).thenReturn(testResponseDto);

        //When
        BookResponseDto result = bookService.getBookById(EXISTING_BOOK_ID);

        //Then
        Assertions.assertEquals(testResponseDto, result,
                "The result should match the expected response DTO.");
        Assertions.assertNotNull(result, "The result should not be null.");

        verify(bookRepository).findByIdWithCategories(EXISTING_BOOK_ID);
        verify(bookMapper).toDto(testBook);

    }

    @Test
    @DisplayName("Get book by non-existing ID should throw EntityNotFoundException")
    void findByNotExisting_Id_NegativeResult() {
        //Given
        when(bookRepository.findByIdWithCategories(NOT_EXISTING_ID))
                .thenThrow(new EntityNotFoundException("can`t find book with id: "
                        + NOT_EXISTING_ID));

        //When
        EntityNotFoundException exception =
                Assertions.assertThrows(EntityNotFoundException.class,
                        () -> {
                            bookService.getBookById(NOT_EXISTING_ID);
                        });

        //Then
        Assertions.assertEquals("can`t find book with id: "
                + NOT_EXISTING_ID, exception.getMessage());
    }

    @Test
    @DisplayName("Update book by ID with valid data should succeed")
    void updateBookById_positiveResult() {
        //Given
        Book testBook = BookTestDataFactory.createBook();
        BookRequestDto testRequestDto = BookTestDataFactory.createBookRequestDto();

        Category testCategoryDrama = CategoryTestDataFactory.createNewCategory_Drama();
        Category testCategoryAction = CategoryTestDataFactory.createNewCategory_Action();
        testRequestDto.setCategoryIds(Set.of(testCategoryDrama.getId(),
                testCategoryAction.getId()));

        Set<Category> categories = Set.of(testCategoryDrama, testCategoryAction);
        List<Category> testCategoryList = List.of(testCategoryDrama, testCategoryAction);

        Set<Long> categoryIds = testRequestDto.getCategoryIds();
        testBook.setCategories(categories);
        BookResponseDto testResponseDto = BookTestDataFactory.createBookResponseDto();

        when(bookRepository.findById(EXISTING_BOOK_ID)).thenReturn(Optional.of(testBook));
        when(categoryRepository.findCategoriesByIds(categoryIds)).thenReturn(testCategoryList);
        when(bookRepository.save(testBook)).thenReturn(testBook);
        when(bookMapper.toDto(testBook)).thenReturn(testResponseDto);

        // When
        BookResponseDto result = bookService.updateBookById(EXISTING_BOOK_ID, testRequestDto);

        // Then
        Assertions.assertEquals(testResponseDto, result);
    }

    @Test
    @DisplayName("Update book by ID with missing categories should throw CategoryException")
    void updateBookById_withMissingCategories_throwsCategoryException() {
        // Given
        Book existingBook = BookTestDataFactory.createBook();
        existingBook.setId(EXISTING_BOOK_ID);

        BookRequestDto updatedBookDto = BookTestDataFactory.createBookRequestDto();
        Set<Long> requestedCategoryIds = Set.of(1L, 2L, 3L);
        updatedBookDto.setCategoryIds(requestedCategoryIds);

        Category category1
                = CategoryTestDataFactory.createNewCategory_Drama();
        Category category2
                = CategoryTestDataFactory.createNewCategory_Action();
        List<Category> foundCategories = List.of(category1, category2);

        when(bookRepository.findById(EXISTING_BOOK_ID)).thenReturn(Optional.of(existingBook));
        when(categoryRepository.findCategoriesByIds(requestedCategoryIds))
                .thenReturn(foundCategories);

        // When
        CategoryException exception = Assertions.assertThrows(CategoryException.class,
                () -> bookService.updateBookById(EXISTING_BOOK_ID, updatedBookDto));

        // Then
        Set<Long> missingCategoryIds = Set.of(3L);
        Assertions.assertEquals("Categories not found with ids: "
                + missingCategoryIds, exception.getMessage());
    }

    @Test
    @DisplayName("Update book by ID with existing ISBN should throw EntityNotFoundException")
    void updateBookById_withExistingIsbn_throwsEntityNotFoundException() {
        // Given
        Book existingBook = BookTestDataFactory.createBook();
        existingBook.setId(EXISTING_BOOK_ID);
        existingBook.setIsbn("111-222-333");

        BookRequestDto updatedBookDto = BookTestDataFactory.createBookRequestDto();
        updatedBookDto.setIsbn(EXISTING_ISBN);

        when(bookRepository.findById(EXISTING_BOOK_ID)).thenReturn(Optional.of(existingBook));
        when(bookRepository.findByIsbn(EXISTING_ISBN)).thenReturn(Optional.of(new Book()));

        // When
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.updateBookById(EXISTING_BOOK_ID, updatedBookDto));

        // Then
        Assertions.assertEquals("Book with ISBN " + EXISTING_ISBN
                + " already exists.", exception.getMessage());
    }

    @Test
    @DisplayName("Delete book by ID should successfully remove the book")
    void deleteById_WithExistingId_positiveResult() {
        // Given
        Book testBook = BookTestDataFactory.createBook();
        when(bookRepository.findById(EXISTING_BOOK_ID)).thenReturn(Optional.of(testBook));

        // When
        bookService.deleteById(EXISTING_BOOK_ID);

        // Then
        verify(bookRepository).findById(EXISTING_BOOK_ID);
        verify(bookRepository).deleteById(EXISTING_BOOK_ID);
    }

    @Test
    @DisplayName("Delete book by non-existing ID should throw EntityNotFoundException")
    void deleteById_WithNonExistingId_negativeResult() {
        // Given
        when(bookRepository.findById(NOT_EXISTING_ID)).thenReturn(Optional.empty());

        // When
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> bookService.deleteById(NOT_EXISTING_ID));
        // Then
        Assertions.assertEquals("Book not found with id: "
                + NOT_EXISTING_ID, exception.getMessage());
        verify(bookRepository).findById(NOT_EXISTING_ID);
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Find all books by category ID should return a list of books without category IDs")
    void findAllByCategoryId_positiveResult() {
        //Given
        Pageable pageable = PageRequest.of(0, 10);
        Book testBook = BookTestDataFactory.createBook();
        List<Book> testBooksList = List.of(testBook);
        BookDtoWithoutCategoryIds testBookDtoWithoutCategoryIds
                = BookTestDataFactory.createBookDtoWithoutCategoryIds();

        List<BookDtoWithoutCategoryIds> bookDtoWithoutCategoryIdsList
                = List.of(testBookDtoWithoutCategoryIds);

        when(bookRepository.findAllByCategoryId(CATEGORY_ID,
                pageable)).thenReturn(testBooksList);
        when(bookMapper.toDtoWithoutCategoryIds(testBook))
                .thenReturn(testBookDtoWithoutCategoryIds);

        //When
        List<BookDtoWithoutCategoryIds> result
                = bookService.findAllByCategoryId(CATEGORY_ID, pageable);

        //Then
        Assertions.assertEquals(bookDtoWithoutCategoryIdsList.size(), result.size());
    }

    @Test
    @DisplayName("Should return positive result when searching for books with given parameters")
    void search_positiveResult() {

        //Given
        Pageable pageable = PageRequest.of(0, 10);

        Book testBook = BookTestDataFactory.createBook();
        List<Book> testBookList = List.of(testBook);
        BookResponseDto testResponseDto = BookTestDataFactory.createBookResponseDto();

        String[] title = new String[]{DRAMA_NAME_OF_BOOK, ACTION_NAME_OF_BOOK};
        String[] author = new String[]{TOLKIEN_AUTHOR_OF_BOOK, LOVECRAFT_AUTHOR_OF_BOOK};

        BookSearchParameters bookSearchParameters
                = new BookSearchParameters(title, author);
        Specification<Book> bookSpecification
                = bookSpecificationBuilder.build(bookSearchParameters);

        when(bookRepository.findAll(bookSpecification)).thenReturn(testBookList);
        when(bookMapper.toDto(testBook)).thenReturn(testResponseDto);
        //When
        List<BookResponseDto> result = bookService.search(bookSearchParameters, pageable);

        //Then
        Assertions.assertEquals(EXPECTED_BOOK_LIST_SIZE, result.size());

    }
}
