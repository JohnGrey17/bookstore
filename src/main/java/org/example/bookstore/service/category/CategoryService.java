package org.example.bookstore.service.category;

import org.example.bookstore.dto.categorydto.CategoryDto;
import org.example.bookstore.model.Category;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CategoryService {
    List<Category> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto categoryDto);

    CategoryDto update(Long id, CategoryDto categoryDto);

    void deleteById(Long id);
}
