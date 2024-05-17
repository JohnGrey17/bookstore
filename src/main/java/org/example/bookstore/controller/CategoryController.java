package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.bookdto.BookResponseDto;
import org.example.bookstore.dto.categorydto.CategoryDto;
import org.example.bookstore.dto.categorydto.CategoryRequestDto;
import org.example.bookstore.service.book.BookService;
import org.example.bookstore.service.category.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Category Controller", description = "This controller handles requests "
        + "and responses related to categories in the database")
public class CategoryController {

    private final CategoryService categoryService;
    private final BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new category", description = "All users with status Admin "
            + "can create new category in DB")
    public CategoryDto createCategory(@RequestBody @Valid CategoryRequestDto requestDto) {
        return categoryService.save(requestDto);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("categories")
    @Operation(summary = "Get all category from DB", description =
            "Get all existing categories from DB")
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("categories/{id}")
    @Operation(summary = "Get category by id", description = "Get existing category by id")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("categories/{id}")
    @Operation(summary = "Update category by id", description = "Get category by id "
            + "and update property")
    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDto
            requestDto) {
        return categoryService.update(id, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("categories/{id}")
    @Operation(summary = "Delete category", description = "Mark category as"
            + " delete and deactivate it")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("categories/{id}/books")
    @Operation(summary = "Get all books by category id", description = "Get all books that "
            + "relate to specified category")
    public List<BookResponseDto> getBooksByCategoryId(@PathVariable Long id, Pageable pageable) {
        return bookService.getBookDtoByCategoryId(id, pageable);
    }
}
