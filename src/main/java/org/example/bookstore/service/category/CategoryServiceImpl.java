package org.example.bookstore.service.category;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.categorydto.CategoryDto;
import org.example.bookstore.dto.categorydto.CategoryRequestDto;
import org.example.bookstore.exception.CategoryException;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.CategoryMapper;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.category.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository
                .findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryRepository.findById(id).stream()
                .map(categoryMapper::toDto)
                .findFirst()
                .orElseThrow(() -> new CategoryException(
                        "cant find category by id: " + id));
    }

    @Override
    public CategoryDto save(CategoryRequestDto requestDto) {
        if (categoryRepository.findByName(requestDto.getName()).isPresent()) {
            throw new CategoryException("That category already exist");
        }
        Category category = categoryMapper.toModel(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto requestDto) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryException(
                        "can`t find category by id: " + id));
        if (requestDto.getName().equals(existingCategory.getName())) {
            throw new CategoryException("Category name for update should be different");
        }
        existingCategory.setName(requestDto.getName());
        existingCategory.setDescription(requestDto.getDescription());
        return categoryMapper.toDto(categoryRepository.save(existingCategory));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "category not found with id: " + id));
        categoryRepository.deleteById(id);
    }
}
