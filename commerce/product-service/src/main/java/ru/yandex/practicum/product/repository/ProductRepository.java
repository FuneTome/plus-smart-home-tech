package ru.yandex.practicum.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.product.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByCategory_IdAndActiveTrue(long id);

    boolean existsByName(String name);

    List<Product> findAllByActiveTrue();

    Product findByIdAndActiveTrue(long id);

    List<Product> searchAllByNameContaining(String name);
}
