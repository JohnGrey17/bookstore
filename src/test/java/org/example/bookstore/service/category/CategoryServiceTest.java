package org.example.bookstore.service.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.example.bookstore.dto.categorydto.CategoryRequestDto;
import org.example.bookstore.dto.categorydto.CategoryResponseDto;
import org.example.bookstore.exception.CategoryException;
import org.example.bookstore.exception.EntityNotFoundException;
import org.example.bookstore.mapper.CategoryMapper;
import org.example.bookstore.model.Category;
import org.example.bookstore.repository.category.CategoryRepository;
import org.example.bookstore.utils.CategoryTestDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    private static final int EXPECTED_LIST_SIZE = 1;
    private static final Long EXISTING_CATEGORY_ID = 1L;
    private static final Long NOT_EXISTING_CATEGORY_ID = -1L;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Find all categories with pagination - Positive result")
    void findAll_positiveResult() {

        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Category testCategory = CategoryTestDataFactory.createNewCategory_Drama();
        CategoryResponseDto testResponseDto = CategoryTestDataFactory.createCategoryResponseDto();

        Page<Category> categoryPage = new PageImpl<>(List.of(testCategory));

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(testCategory)).thenReturn(testResponseDto);

        // When
        List<CategoryResponseDto> result = categoryService.findAll(pageable);

        // Then
        Assertions.assertEquals(EXPECTED_LIST_SIZE, result.size());
        Assertions.assertEquals(testResponseDto, result.get(0));

    }

    @Test
    @DisplayName("Get category by existing ID - Positive result")
    void getById_WithExisting_Id_PositiveResult() {

        // Given
        Category testCategory = CategoryTestDataFactory.createNewCategory_Drama();
        CategoryResponseDto testResponseDto = CategoryTestDataFactory.createCategoryResponseDto();

        when(categoryRepository.findById(EXISTING_CATEGORY_ID))
                .thenReturn(Optional.of(testCategory));
        when(categoryMapper.toDto(testCategory)).thenReturn(testResponseDto);

        //When
        CategoryResponseDto result = categoryService.getById(EXISTING_CATEGORY_ID);

        //Then
        Assertions.assertEquals(testResponseDto, result,
                "Category should much " + testResponseDto);

    }

    @Test
    @DisplayName("Get category by non-existing ID - Negative result")
    void getById_withNotWrongId_NegativeResult() {

        // Given
        when(categoryRepository.findById(NOT_EXISTING_CATEGORY_ID))
                .thenThrow(new CategoryException("cant find category by id: "
                        + NOT_EXISTING_CATEGORY_ID));

        // When
        CategoryException exception = Assertions.assertThrows(CategoryException.class, () -> {
            categoryService.getById(NOT_EXISTING_CATEGORY_ID);
        });

        Assertions.assertEquals("cant find category by id: -1", exception.getMessage());

    }

    @Test
    @DisplayName("Save a new category - Positive result")
    void saveCategory_PositiveResult() {
        //Given
        Category testCategory = CategoryTestDataFactory.createNewCategory_Drama();
        CategoryRequestDto testRequestDto
                = CategoryTestDataFactory.createCategoryRequestDto();
        CategoryResponseDto testResponseDto
                = CategoryTestDataFactory.createCategoryResponseDto();

        when(categoryMapper.toModel(testRequestDto)).thenReturn(testCategory);
        when(categoryRepository.save(testCategory)).thenReturn(testCategory);
        when(categoryMapper.toDto(testCategory)).thenReturn(testResponseDto);

        //When
        CategoryResponseDto result = categoryService.save(testRequestDto);

        //Then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(testResponseDto, result);
    }

    @Test
    @DisplayName("Save a category that already exists - Negative result")
    void saveCategory_WhichIsAlreadyExist_NegativeResult() {
        //Given
        CategoryRequestDto testRequestDto = CategoryTestDataFactory.createCategoryRequestDto();
        Category testCategory = CategoryTestDataFactory.createNewCategory_Drama();

        when(categoryRepository.findByName(testRequestDto.getName()))
                .thenReturn(Optional.of(testCategory));

        //When
        CategoryException exception = Assertions.assertThrows(CategoryException.class, () -> {
            categoryService.save(testRequestDto);
        });

        //Then
        Assertions.assertEquals("That category already exist", exception.getMessage());
    }

    @Test
    @DisplayName("Update category with existing parameter - Positive result")
    void updateWithExistingParameter_PositiveResult() {
        // Given
        Category testCategory = CategoryTestDataFactory.createNewCategory_Drama();
        CategoryResponseDto testResponseDto
                = CategoryTestDataFactory.createCategoryResponseDto();
        CategoryRequestDto testRequestDto
                = CategoryTestDataFactory.createRequestDtoForUpdateMethod();

        when(categoryRepository.findById(EXISTING_CATEGORY_ID))
                .thenReturn(Optional.of(testCategory));

        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        when(categoryMapper.toDto(testCategory)).thenReturn(testResponseDto);

        // When
        CategoryResponseDto result = categoryService.update(EXISTING_CATEGORY_ID, testRequestDto);

        // Then
        Assertions.assertEquals(testResponseDto, result);
        Assertions.assertNotNull(result);

        Assertions.assertEquals(testRequestDto.getName(), testCategory.getName());
        Assertions.assertEquals(testRequestDto.getDescription(), testCategory.getDescription());

        verify(categoryRepository).save(testCategory);
    }

    @Test
    @DisplayName("Update category with non-existing ID - Negative result")
    void update_Category_with_notExistingId_negativeResult() {
        //Given
        CategoryRequestDto testRequestDto = CategoryTestDataFactory
                .createRequestDtoForUpdateMethod();
        when(categoryRepository.findById(NOT_EXISTING_CATEGORY_ID))
                .thenReturn(Optional.empty());

        // When
        CategoryException exception = Assertions.assertThrows(CategoryException.class, () -> {
            categoryService.update(NOT_EXISTING_CATEGORY_ID, testRequestDto);
        });

        //Then
        Assertions.assertEquals("can`t find category by id: "
                + NOT_EXISTING_CATEGORY_ID, exception.getMessage());
    }

    @Test
    @DisplayName("Update category with the same name - Negative result")
    void updateCategory_WithSameName_negativeResult() {
        //Given

        CategoryRequestDto testRequestDto = CategoryTestDataFactory.createCategoryRequestDto();
        Category testCategory = CategoryTestDataFactory.createNewCategory_Drama();

        when(categoryRepository.findById(EXISTING_CATEGORY_ID))
                .thenReturn(Optional.of(testCategory));

        // When
        CategoryException exception = Assertions.assertThrows(CategoryException.class, () -> {
            categoryService.update(EXISTING_CATEGORY_ID, testRequestDto);
        });

        //Then
        Assertions.assertEquals("Category name for update should be different",
                exception.getMessage());
    }

    @Test
    @DisplayName("Delete category by existing ID - Positive result")
    void deleteById_positiveResult() {
        //Given
        when(categoryRepository.existsById(EXISTING_CATEGORY_ID)).thenReturn(true);

        //When
        categoryService.deleteById(EXISTING_CATEGORY_ID);

        // Then
        verify(categoryRepository, times(1)).deleteById(EXISTING_CATEGORY_ID);
    }

    @Test
    @DisplayName("Delete category by non-existing ID - Negative result")
    void deleteById_NotExistingCategory_NegativeResult() {
        // Given
        when(categoryRepository.existsById(NOT_EXISTING_CATEGORY_ID)).thenReturn(false);

        // When
        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class,
                () -> {
                    categoryService.deleteById(NOT_EXISTING_CATEGORY_ID);
                });

        // Then
        Assertions.assertEquals("Category not found with id: "
                + NOT_EXISTING_CATEGORY_ID, exception.getMessage());
        verify(categoryRepository, never()).deleteById(NOT_EXISTING_CATEGORY_ID);
    }
}
