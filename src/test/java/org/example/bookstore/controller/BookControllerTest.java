package org.example.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Collections;
import org.example.bookstore.dto.bookdto.BookRequestDto;
import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @WithMockUser(username = "user", roles = {"ADMIN"})
    @Test

    @DisplayName("Create a new book")
    void createBook_ValidRequestDto_Success() throws Exception {

        BookRequestDto bookRequestDto = new BookRequestDto();
        bookRequestDto.setTitle("Test Book");
        bookRequestDto.setPrice(BigDecimal.valueOf(100.0));
        bookRequestDto.setAuthor("Test Author");
        bookRequestDto.setIsbn("978-3-16-148410-0");
        bookRequestDto.setCategoryIds(Collections.singleton(1L));

        BookResponseDto expected = new BookResponseDto();
        expected.setTitle(bookRequestDto.getTitle());
        expected.setAuthor(bookRequestDto.getAuthor());
        expected.setPrice(bookRequestDto.getPrice());
        expected.setIsbn(bookRequestDto.getIsbn());


    }
}
