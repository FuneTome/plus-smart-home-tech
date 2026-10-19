package ru.yandex.practicum.product.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.product.dto.CreateProductRequest;
import ru.yandex.practicum.product.dto.ProductDto;
import ru.yandex.practicum.product.dto.UpdateProductRequest;
import ru.yandex.practicum.product.service.ProductService;

import java.util.List;

@RequestMapping("/api/products")
@RestController
@AllArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable long id) {
        return productService.getProduct(id);
    }

    @GetMapping("/search")
    public List<ProductDto> search(@RequestParam String query) {
        return productService.search(query);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductDto> getProductsByCategory(@PathVariable long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody @Valid CreateProductRequest product) {
        return productService.createProduct(product);
    }

    @PatchMapping("/{id}")
    public ProductDto updateProduct(@RequestBody @Valid UpdateProductRequest product,
                                    @PathVariable long id) {
        return productService.updateProduct(product, id);
    }
}
