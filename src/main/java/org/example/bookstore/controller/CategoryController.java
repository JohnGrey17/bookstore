package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.example.bookstore.dto.categorydto.CategoryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Category Controller",description = "This controller handles requests "
        + "and responses related to categories in the database")
public class CategoryController {
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/categories")
    public CategoryDto createCategory(@RequestBody CategoryDto categoryDto) {
        return null;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/api/categories")
    public List<CategoryDto> getAll(Pageable pageable) {
        return null;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/api/categories/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/categories/{id}")
    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        return null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/categories/{id}")
    public void deleteCategory(@PathVariable Long id) {
        return;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/api/categories/{id}/books")
    public List<BookResponseDto> getBooksByCategoryId(@PathVariable Long id) {
       return  null;
    }
}