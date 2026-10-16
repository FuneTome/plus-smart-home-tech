package ru.yandex.practicum.inventory.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inventory")
@Data
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "reserved_quantity")
    private int reservedQuantity;

    @Column(name = "available_quantity")
    private int availableQuantity;

    @Column(name = "version")
    @Version
    private long version;
}
