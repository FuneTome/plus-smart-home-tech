package ru.yandex.practicum.inventory.service;

import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.ReserveRequest;
import ru.yandex.practicum.inventory.dto.ReserveResponse;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;

import java.util.List;

public interface InventoryService {
    List<InventoryDto> getAllRemains();

    InventoryDto getRemain(long productId);

    InventoryDto updateRemains(UpdateInventoryRequest request);

    InventoryDto createRemain(UpdateInventoryRequest request);

    ReserveResponse reserveProduct(ReserveRequest request);
}
