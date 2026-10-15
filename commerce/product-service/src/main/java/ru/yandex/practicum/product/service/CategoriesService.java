package ru.yandex.practicum.product.service;

import ru.yandex.practicum.product.dto.CategoryDto;
import ru.yandex.practicum.product.dto.CreateCategoryRequest;

import java.util.List;

public interface CategoriesService {
    List<CategoryDto> getCategories();

    CategoryDto getCategory(long id);

    CategoryDto createCategory(CreateCategoryRequest category);
}
