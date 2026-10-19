package ru.yandex.practicum.product.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.product.dto.CategoryDto;
import ru.yandex.practicum.product.dto.CreateCategoryRequest;
import ru.yandex.practicum.product.dto.mapper.CategoryMapper;
import ru.yandex.practicum.product.entity.Category;
import ru.yandex.practicum.product.exception.ConflictException;
import ru.yandex.practicum.product.exception.NotFoundException;
import ru.yandex.practicum.product.repository.CategoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoriesService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    @Override
    public List<CategoryDto> getCategories() {
        return categoryRepository.findAll().stream()
                .map(mapper::toCategoryDto)
                .toList();
    }

    @Override
    public CategoryDto getCategory(long id) {
        return mapper.toCategoryDto(categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id = " + id + " не найдена" )));
    }

    @Override
    public CategoryDto createCategory(CreateCategoryRequest cat) {
        Category category = mapper.toCategory(cat);
        if(categoryRepository.existsByName(category.getName())) {
            throw new ConflictException("Категория с name = " + category.getName() + " уже существует");
        }
        return mapper.toCategoryDto(categoryRepository.save(category));
    }
}
