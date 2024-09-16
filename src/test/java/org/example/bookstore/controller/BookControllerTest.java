package org.example.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import javax.sql.DataSource;
import org.example.bookstore.dto.bookdto.BookRequestDto;
import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.example.bookstore.utils.BookTestDataFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    public static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(10);
    private static final String VALID_TITLE = "Valid title";
    private static final String SEARCH_PARAM_TITLE = "Test book_1";
    private static final String SEARCH_PARAM_AUTHOR = "Test Author_1";
    private static final String VALID_AUTHOR = "Valid Author";
    private static final String VALID_ISBN = "978-3-16-148410-0";
    private static final String VALID_DESCRIPTION = "Valid Description";
    private static final String VALID_COVER_IMAGE = "Valid Cover Image URL";
    private static final Set<Long> CATEGORIES_ID = Set.of(1L, 2L);
    private static final int EXPECTED_BOOK_ARRAY_SIZE = 2;
    private static final Object BOOK_ID = 10L;
    private static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void beforeEach(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/book/add-books-to-books-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/categories/add-categories-to-categories-table.sql"));
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(
                    "database/categories_books/add-categories-to-books.sql"));
        }
    }

    @AfterEach
    void afterEach(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/categories_books/delete-categories-books-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/categories/remove-categories-from-books-table.sql"));
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource(
                            "database/book/remove-books-from-books-table.sql"));
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create new Book")
    void createBook_ValidRequestDto_Ok() throws Exception {

        //Given
        BookRequestDto bookRequestDto = new BookRequestDto();
        bookRequestDto.setTitle(VALID_TITLE);
        bookRequestDto.setAuthor(VALID_AUTHOR);
        bookRequestDto.setIsbn(VALID_ISBN);
        bookRequestDto.setPrice(BOOK_PRICE);
        bookRequestDto.setDescription(VALID_DESCRIPTION);
        bookRequestDto.setCoverImage(VALID_COVER_IMAGE);
        bookRequestDto.setCategoryIds(CATEGORIES_ID);

        BookResponseDto expected = new BookResponseDto();
        expected.setTitle(bookRequestDto.getTitle());
        expected.setAuthor(bookRequestDto.getAuthor());
        expected.setIsbn(bookRequestDto.getIsbn());
        expected.setPrice(bookRequestDto.getPrice());
        expected.setDescription(bookRequestDto.getDescription());
        expected.setCoverImage(bookRequestDto.getCoverImage());
        expected.setCategoryIds(bookRequestDto.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(bookRequestDto);

        //When
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        //Then
        BookResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookResponseDto.class);

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    @DisplayName("Get all existing Books from repository")
    void getAllBookFromRepository_PositiveResult() throws Exception {

        //When
        MvcResult result = mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String jsonResponse = result.getResponse().getContentAsString();
        BookResponseDto[] actualBooks = objectMapper.readValue(
                jsonResponse, BookResponseDto[].class);

        Assertions.assertEquals(EXPECTED_BOOK_ARRAY_SIZE, actualBooks.length);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    @DisplayName("Find existing Book by id")
    void getBookById_positive_result() throws Exception {
        //Given
        BookResponseDto bookResponseDto = BookTestDataFactory.createResponseDtoForController();

        String expected = objectMapper.writeValueAsString(bookResponseDto);

        //When
        MvcResult result = mockMvc.perform(get("/books/{id}", BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookResponseDto actualResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class);

        EqualsBuilder.reflectionEquals(expected, actualResponseDto, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get all existing Books from repository")
    void updateBookById_PositiveResult() throws Exception {

        // Given:
        BookRequestDto bookRequestDto = BookTestDataFactory.createUpdatedBookRequestDto();

        BookResponseDto expectedResponseDto = BookTestDataFactory.createUpdatedBookResponseDto();

        String jsonString = objectMapper.writeValueAsString(bookRequestDto);

        MvcResult result = mockMvc.perform(put("/books/{id}", BOOK_ID)
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then:
        BookResponseDto actualResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto.class);

        EqualsBuilder.reflectionEquals(expectedResponseDto, actualResponseDto, "id");

    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    @DisplayName("Get all existing Books by searchParameter")
    void searchBookByParameters_positiveResult() throws Exception {

        //When
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("title", SEARCH_PARAM_TITLE)
                        .param("author", SEARCH_PARAM_AUTHOR)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String jsonResponse = result.getResponse().getContentAsString();
        BookResponseDto[] actualBooks = objectMapper.readValue(
                jsonResponse, BookResponseDto[].class);

        Assertions.assertEquals(1, actualBooks.length);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get all existing Books by searchParameter")
    void delete_bookById_positiveResult() throws Exception {

        //When
        MvcResult result = mockMvc.perform(delete("/books/{id}", BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        //Then
        boolean blank = result.getResponse().getContentAsString().isBlank();

        Assertions.assertTrue(blank);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Try to delete book with USER role should fail")
    void deleteBookById_WithUserRole_Forbidden() throws Exception {
        mockMvc.perform(delete("/books/{id}", BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
