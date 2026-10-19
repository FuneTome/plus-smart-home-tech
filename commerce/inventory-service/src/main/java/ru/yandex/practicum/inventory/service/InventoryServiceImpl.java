package ru.yandex.practicum.inventory.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.ReserveRequest;
import ru.yandex.practicum.inventory.dto.ReserveResponse;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;
import ru.yandex.practicum.inventory.dto.mapper.InventoryMapper;
import ru.yandex.practicum.inventory.entity.Inventory;
import ru.yandex.practicum.inventory.exception.InsufficientStockException;
import ru.yandex.practicum.inventory.exception.NotFoundException;
import ru.yandex.practicum.inventory.repository.InventoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper mapper;

    @Override
    public List<InventoryDto> getAllRemains() {
        return inventoryRepository.findAll().stream()
                .map(mapper::toInventoryDto)
                .toList();
    }

    @Override
    public InventoryDto getRemain(long productId) {
        return mapper.toInventoryDto(inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("Остатки с id = " + productId + " не найдены")));
    }

    @Override
    public InventoryDto updateRemains(UpdateInventoryRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(request.productId())
                .orElseThrow(() -> new NotFoundException("Остатки с id = " + request.productId() + " не найдены"));
        inventory.setQuantity(request.quantity());
        inventory.setAvailableQuantity(request.quantity() - inventory.getReservedQuantity());
        return mapper.toInventoryDto(inventoryRepository.save(inventory));
    }

    @Override
    public InventoryDto createRemain(UpdateInventoryRequest request) {
        Inventory inventory = mapper.toInventory(request);

        if (inventoryRepository.existsByProductId(request.productId())) {
            throw new InsufficientStockException("Остатки для productId уже существуют");
        }

        inventory.setReservedQuantity(0);
        inventory.setAvailableQuantity(request.quantity());
        return mapper.toInventoryDto(inventoryRepository.save(inventory));
    }

    @Override
    public ReserveResponse reserveProduct(ReserveRequest request) {
        Inventory inventory = inventoryRepository.findByProductId(request.productId())
                .orElseThrow(() -> new NotFoundException("Остатки с id = " + request.productId() + " не найдена"));

        int newReserve = request.quantity() + inventory.getReservedQuantity();

        if(inventory.getQuantity() < newReserve) {
            throw new InsufficientStockException("Количество для резервирования больше чем количество товара на складе");
        }
        inventory.setReservedQuantity(newReserve);
        inventory.setAvailableQuantity(inventory.getQuantity() - newReserve);
        inventoryRepository.save(inventory);
        return new ReserveResponse(true, inventory.getAvailableQuantity(), "Товар успешно зарезервирован");
    }
}
