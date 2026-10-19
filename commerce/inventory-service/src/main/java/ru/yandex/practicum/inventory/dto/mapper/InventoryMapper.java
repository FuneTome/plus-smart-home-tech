package ru.yandex.practicum.inventory.dto.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;
import ru.yandex.practicum.inventory.entity.Inventory;

@Mapper(componentModel = "spring")
public interface InventoryMapper {
    Inventory toInventory(UpdateInventoryRequest request);

    InventoryDto toInventoryDto (Inventory inventory);
}
