package org.example.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.example.bookstore.dto.categorydto.CategoryRequestDto;
import org.example.bookstore.dto.categorydto.CategoryResponseDto;
import org.example.bookstore.model.Category;
import org.example.bookstore.utils.CategoryTestDataFactory;
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
class CategoryControllerTest {

    private static final int EXPECTED_BOOK_ARRAY_SIZE = 7;
    private static final Long CATEGORY_ID = 10L;

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
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Create new category")
    void createCategory_positive_result() throws Exception {
        //Given
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("Test category name");
        categoryRequestDto.setDescription("Test category description");

        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setName(categoryRequestDto.getName());
        expected.setDescription(categoryRequestDto.getDescription());

        String jsonString = objectMapper.writeValueAsString(categoryRequestDto);

        //When
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        //Then
        BookResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookResponseDto.class);

        EqualsBuilder.reflectionEquals(expected, actual, "id");
        Assertions.assertNotNull(actual);
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN", "USER"})
    @DisplayName("Get all categories")
    void getAllCategories_positive_result() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BookResponseDto[] actualBooks = objectMapper.readValue(
                jsonResponse, BookResponseDto[].class);

        Assertions.assertEquals(EXPECTED_BOOK_ARRAY_SIZE, actualBooks.length);
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN", "USER"})
    @DisplayName("Get category by ID")
    void getCategoryById_positive_result() throws Exception {

        //Given
        Category expected = CategoryTestDataFactory.createNewCategory_Drama();

        //When
        MvcResult result = mockMvc.perform(get("/categories/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookResponseDto actual = objectMapper.readValue(result
                .getResponse().getContentAsString(), BookResponseDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Update category")
    void updateCategory_positive_result() throws Exception {
        //Given
        CategoryRequestDto updatedCategory = new CategoryRequestDto();
        updatedCategory.setName("Updated category name");
        updatedCategory.setDescription("Updated category description");

        CategoryResponseDto expected = CategoryTestDataFactory.createCategoryResponseDto();

        String jsonString = objectMapper.writeValueAsString(updatedCategory);

        //When
        MvcResult result = mockMvc.perform(put("/categories/{id}", CATEGORY_ID)
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");

    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Delete category by ID")
    void deleteCategory_positive_result() throws Exception {

        mockMvc.perform(delete("/categories/{id}", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    @DisplayName("Get books by category ID")
    void getBooksByCategory_positive_result() throws Exception {

        //When
        MvcResult result = mockMvc.perform(get("/categories/{id}/books", CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookResponseDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookResponseDto[].class);

        Assertions.assertEquals(1, actual.length);
    }
}
