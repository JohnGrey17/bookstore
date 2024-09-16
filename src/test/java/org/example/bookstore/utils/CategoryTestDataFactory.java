package org.example.bookstore.utils;

import org.example.bookstore.dto.categorydto.CategoryRequestDto;
import org.example.bookstore.dto.categorydto.CategoryResponseDto;
import org.example.bookstore.model.Category;

public class CategoryTestDataFactory {

    private static final String CATEGORY_NAME_DRAMA = "Drama";
    private static final String CATEGORY_NAME_ACTION = "Action";
    private static final String CATEGORY_DESCRIPTION = "Test Description";
    private static final Long FIRST_CATEGORY_ID = 1L;
    private static final Long SECOND_CATEGORY_ID = 2L;
    private static final String NEW_CATEGORY_NAME_FOR_POST = "Test category name";

    public static Category createNewCategory_Drama() {
        Category category = new Category();
        category.setId(FIRST_CATEGORY_ID);
        category.setName(CATEGORY_NAME_DRAMA);
        category.setDescription(CATEGORY_DESCRIPTION);
        return category;
    }

    public static Category createNewCategory_Action() {
        Category category = new Category();
        category.setId(SECOND_CATEGORY_ID);
        category.setName(CATEGORY_NAME_ACTION);
        category.setDescription(CATEGORY_DESCRIPTION);
        return category;
    }

    public static CategoryResponseDto createCategoryResponseDto() {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(1L);
        categoryResponseDto.setName(CATEGORY_NAME_DRAMA);
        categoryResponseDto.setDescription(CATEGORY_DESCRIPTION);
        return categoryResponseDto;
    }

    public static CategoryRequestDto createCategoryRequestDto() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName(CATEGORY_NAME_DRAMA);
        categoryRequestDto.setDescription(CATEGORY_DESCRIPTION);
        return categoryRequestDto;
    }

    public static CategoryRequestDto createRequestDtoForUpdateMethod() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName(CATEGORY_NAME_ACTION);
        categoryRequestDto.setDescription(CATEGORY_DESCRIPTION);
        return categoryRequestDto;
    }

    public static CategoryRequestDto createNewRequestToPost() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName(NEW_CATEGORY_NAME_FOR_POST);
        categoryRequestDto.setDescription(CATEGORY_DESCRIPTION);
        return categoryRequestDto;

    }

    public static CategoryResponseDto createNewResponseForPostMethod() {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setName(NEW_CATEGORY_NAME_FOR_POST);
        categoryResponseDto.setDescription(CATEGORY_DESCRIPTION);
        return categoryResponseDto;
    }
}
