package ru.yandex.practicum.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.inventory.entity.Inventory;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    boolean existsByProductId(long productId);

    Optional<Inventory> findByProductId(long productId);
}
