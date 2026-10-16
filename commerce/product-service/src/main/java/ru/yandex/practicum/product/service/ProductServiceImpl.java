package ru.yandex.practicum.product.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.dto.UpdateProductRequest;
import ru.yandex.practicum.product.dto.mapper.ProductMapper;
import ru.yandex.practicum.product.entity.Category;
import ru.yandex.practicum.product.entity.Product;
import ru.yandex.practicum.product.exception.ConflictException;
import ru.yandex.practicum.product.exception.NotFoundException;
import ru.yandex.practicum.product.repository.CategoryRepository;
import ru.yandex.practicum.product.repository.ProductRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper mapper;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAllByActiveTrue().stream()
                .map(mapper::toProductDto)
                .toList();
    }

    @Override
    public List<ProductDto> search(String query) {
        return productRepository.searchAllByNameContaining(query).stream()
                .map(mapper::toProductDto)
                .toList();
    }

    @Override
    public ProductDto getProduct(long id) {
        Product pr = productRepository.findByIdAndActiveTrue(id);
        if(pr == null) {
            throw new NotFoundException("Продукт с id = " + id + " не найден" );
        }
        return mapper.toProductDto(pr);
    }

    @Override
    public List<ProductDto> getProductsByCategory(long categoryId) {
        return productRepository.findAllByCategory_IdAndActiveTrue(categoryId).stream()
                .map(mapper::toProductDto)
                .toList();
    }

    @Override
    public ProductDto createProduct(CreateProductRequest prod) {
        Product product = mapper.toProduct(prod);

        if (prod.categoryId() != null) {
            Category category = categoryRepository.findById(prod.categoryId())
                    .orElseThrow(() -> new NotFoundException(
                            "Категория с id = " + prod.categoryId() + " не найдена"));
            product.setCategory(category);
        }

        if(productRepository.existsByName(product.getName())) {
            throw new ConflictException("Товар с name = " + product.getName() + " уже существует");
        }
        return mapper.toProductDto(productRepository.save(product));
    }

    @Override
    public ProductDto updateProduct(UpdateProductRequest prod, long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Продукт с id = " + id + " не найден"));
        if (prod.name() != null) {
            if (!prod.name().equals(product.getName())
                    && productRepository.existsByName(prod.name())) {
                throw new ConflictException("Товар с name = " + prod.name() + " уже существует");
            }
            product.setName(prod.name());
        }

        if (prod.categoryId() != null) {
            Category category = categoryRepository.findById(prod.categoryId())
                    .orElseThrow(() -> new NotFoundException(
                            "Категория с id = " + prod.categoryId() + " не найдена"));
            product.setCategory(category);
        }

        if (prod.description() != null) {
            product.setDescription(prod.description());
        }

        if (prod.price() != null) {
            product.setPrice(prod.price());
        }

        if (prod.imageUrl() != null) {
            product.setImageUrl(prod.imageUrl());
        }

        if (prod.active() != null) {
            product.setActive(prod.active());
        }
        if (prod.categoryId() != null) {
            Category category = categoryRepository.findById(prod.categoryId())
                    .orElseThrow(() -> new NotFoundException(
                            "Категория с id = " + prod.categoryId() + " не найдена"));
            product.setCategory(category);
        }

        return mapper.toProductDto(productRepository.save(product));
    }
}
