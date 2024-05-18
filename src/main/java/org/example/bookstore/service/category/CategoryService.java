package org.example.bookstore.service.category;

import java.util.List;
import org.example.bookstore.dto.categorydto.CategoryRequestDto;
import org.example.bookstore.dto.categorydto.CategoryResponseDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto requestDto);

    CategoryResponseDto update(Long id, CategoryRequestDto requestDto);

    void deleteById(Long id);

}
