package ru.yandex.practicum.inventory.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.inventory.dto.InventoryDto;
import ru.yandex.practicum.inventory.dto.ReserveRequest;
import ru.yandex.practicum.inventory.dto.ReserveResponse;
import ru.yandex.practicum.inventory.dto.UpdateInventoryRequest;
import ru.yandex.practicum.inventory.service.InventoryService;

import java.util.List;

@AllArgsConstructor
@RequestMapping("/api/inventory")
@RestController
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping
    public List<InventoryDto> getAllRemains() {
        return inventoryService.getAllRemains();
    }

    @GetMapping("/{productId}")
    public InventoryDto getRemain(@PathVariable long productId) {
        return inventoryService.getRemain(productId);
    }

    @PutMapping
    public InventoryDto updateRemains(@RequestBody @Valid UpdateInventoryRequest request) {
        return inventoryService.updateRemains(request);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryDto createRemain(@RequestBody @Valid UpdateInventoryRequest request) {
        return inventoryService.createRemain(request);
    }

    @PostMapping("/reserve")
    public ReserveResponse reserveProduct(@RequestBody @Valid ReserveRequest request) {
        return inventoryService.reserveProduct(request);
    }
}
