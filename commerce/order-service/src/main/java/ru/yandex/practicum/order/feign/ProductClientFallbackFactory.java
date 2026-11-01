package ru.yandex.practicum.order.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.order.exception.ProductServiceUnavailableException;

@Slf4j
@Component
public class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {
    @Override
    public ProductClient create(Throwable cause) {
        return id -> {
            log.error("Сбой при запросе к product-service для товара id={}. Причина: {}", id, cause.getMessage());
            throw new ProductServiceUnavailableException(id, cause);
        };
    }
}