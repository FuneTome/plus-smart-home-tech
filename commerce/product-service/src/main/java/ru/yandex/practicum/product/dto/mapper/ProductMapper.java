package ru.yandex.practicum.product.dto.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(CreateProductRequest request);

    ProductDto toProductDto(Product product);
}
