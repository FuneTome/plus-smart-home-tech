package ru.yandex.practicum.product.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.product.dto.CategoryDto;
import ru.yandex.practicum.product.dto.CreateCategoryRequest;
import ru.yandex.practicum.product.service.CategoriesService;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/categories")
@RestController
public class CategoriesController {
    private final CategoriesService categoriesService;

    @GetMapping
    public List<CategoryDto> getCategories() {
        return categoriesService.getCategories();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable long id) {
        return categoriesService.getCategory(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CreateCategoryRequest category) {
        return categoriesService.createCategory(category);
    }
}
