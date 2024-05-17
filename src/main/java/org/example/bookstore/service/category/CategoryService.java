package org.example.bookstore.service.category;

import java.util.List;
import org.example.bookstore.dto.categorydto.CategoryDto;
import org.example.bookstore.dto.categorydto.CategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto requestDto);

    CategoryDto update(Long id, CategoryRequestDto requestDto);

    void deleteById(Long id);

}
