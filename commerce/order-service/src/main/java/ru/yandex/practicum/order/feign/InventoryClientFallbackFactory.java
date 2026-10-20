package ru.yandex.practicum.order.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.order.dto.ReserveRequest;
import ru.yandex.practicum.order.dto.ReserveResponse;
import ru.yandex.practicum.order.exception.InventoryServiceUnavailableException;

@Slf4j
@Component
public class InventoryClientFallbackFactory implements FallbackFactory<InventoryClient> {
    @Override
    public InventoryClient create(Throwable cause) {
        return new InventoryClient() {
            @Override
            public ReserveResponse reserveStock(ReserveRequest request) {
                log.error("Сбой при резервировании товара id={}. Причина: {}", request.productId(), cause.getMessage());
                throw new InventoryServiceUnavailableException(request.productId(), cause);
            }

            @Override
            public void unreserveStock(ReserveRequest request) {
                log.error("Сбой при отмене резерва товара id={}. Причина: {}", request.productId(), cause.getMessage());
                throw new InventoryServiceUnavailableException(request.productId(), cause);
            }
        };
    }
}