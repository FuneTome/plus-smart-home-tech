package ru.yandex.practicum.product.service;

import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.dto.UpdateProductRequest;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();

    List<ProductDto> search(String query);

    ProductDto getProduct(long id);

    List<ProductDto> getProductsByCategory(long categoryId);

    ProductDto createProduct(CreateProductRequest product);

    ProductDto updateProduct(UpdateProductRequest product, long id);
}
