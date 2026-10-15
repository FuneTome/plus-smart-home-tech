package ru.yandex.practicum.product.dto.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.product.dto.CategoryDto;
import ru.yandex.practicum.product.dto.CreateCategoryRequest;
import ru.yandex.practicum.product.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CreateCategoryRequest request);

    CategoryDto toCategoryDto(Category category);
}
