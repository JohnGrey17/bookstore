package org.example.bookstore.service.category;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.categorydto.CategoryRequestDto;
import org.example.bookstore.dto.categorydto.CategoryResponseDto;
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
    public List<CategoryResponseDto> findAll(Pageable pageable) {
        return categoryRepository
                .findAll(pageable)
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        return categoryRepository.findById(id).stream()
                .map(categoryMapper::toDto)
                .findFirst()
                .orElseThrow(() -> new CategoryException(
                        "cant find category by id: " + id));
    }

    @Override
    public CategoryResponseDto save(CategoryRequestDto requestDto) {
        if (categoryRepository.findByName(requestDto.getName()).isPresent()) {
            throw new CategoryException("That category already exist");
        }
        Category category = categoryMapper.toModel(requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryResponseDto update(Long id, CategoryRequestDto requestDto) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryException("can`t find category by id: " + id));
        if (requestDto.getName().equals(existingCategory.getName())) {
            throw new CategoryException("Category name for update should be different");
        }
        existingCategory.setName(requestDto.getName());
        existingCategory.setDescription(requestDto.getDescription());
        return categoryMapper.toDto(categoryRepository.save(existingCategory));
    }

    @Override
    public void deleteById(Long id) {
        checkCategoryExists(id);
        categoryRepository.deleteById(id);
    }

    private void checkCategoryExists(Long id) {
        boolean exists = categoryRepository.existsById(id);
        if (!exists) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
    }
}
