package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.categorydto.CategoryDto;
import org.example.bookstore.dto.categorydto.CategoryRequestDto;
import org.example.bookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {

    CategoryDto toDto(Category category);

    Category toModel(CategoryRequestDto categoryDto);
}
