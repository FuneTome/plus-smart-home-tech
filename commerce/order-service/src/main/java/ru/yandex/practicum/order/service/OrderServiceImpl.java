package ru.yandex.practicum.order.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.order.dto.*;
import ru.yandex.practicum.order.dto.mapper.OrderMapper;
import ru.yandex.practicum.order.entity.Item;
import ru.yandex.practicum.order.entity.Order;
import ru.yandex.practicum.order.exception.InventoryServiceUnavailableException;
import ru.yandex.practicum.order.exception.NotFoundException;
import ru.yandex.practicum.order.exception.OrderProcessingException;
import ru.yandex.practicum.order.exception.ProductServiceUnavailableException;
import ru.yandex.practicum.order.feign.InventoryClient;
import ru.yandex.practicum.order.feign.ProductClient;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto getOrder(long id) {
        return orderMapper.toOrderDto(orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ с id = " + id + " не найден")));
    }

    @Override
    public List<OrderDto> getOrdersByEmail(String email) {
        //не проверяем существует ли почта тк нам впринципе не важно существует ли такая почта
        // да и есть ли на эту почту заказы
        return orderRepository.findAllByCustomerEmail(email).stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }


    @Override
    @Transactional
    public OrderDto createOrder(CreateOrderRequest request) {
        Map<Long, Integer> productQuantities = new HashMap<>();
        for (OrderItemRequest item : request.items()) {
            productQuantities.merge(item.productId(), item.quantity(), Integer::sum);
        }

        boolean isPendingConfirmation = false;
        List<String> degradationReasons = new ArrayList<>();
        Map<Long, ProductDto> productCache = new HashMap<>();

        for (Long productId : productQuantities.keySet()) {
            try {
                ProductDto product = productClient.getProductById(productId);
                if (Boolean.FALSE.equals(product.active())) {
                    throw new OrderProcessingException("Товар с id = " + productId + " снят с продажи");
                }
                productCache.put(productId, product);
            } catch (ProductServiceUnavailableException e) {
                isPendingConfirmation = true;
                degradationReasons.add("Каталог недоступен");
            } catch (FeignException.NotFound e) {
                throw new OrderProcessingException("Товар с id = " + productId + " не найден в каталоге");
            }
        }

        List<ReserveRequest> successfulReserves = new ArrayList<>();
        OrderProcessingException businessException = null;

        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Long productId = entry.getKey();
            ReserveRequest reserveRequest = new ReserveRequest(productId, entry.getValue());

            try {
                inventoryClient.reserveStock(reserveRequest);
                successfulReserves.add(reserveRequest);
            } catch (InventoryServiceUnavailableException e) {
                isPendingConfirmation = true;
                degradationReasons.add("Склад недоступен");
                break;
            } catch (FeignException e) {
                if (e.status() == 409 || e.status() == 404) {
                    businessException = new OrderProcessingException("Не удалось зарезервировать товар: " + e.contentUTF8());
                    break;
                } else {
                    isPendingConfirmation = true;
                    degradationReasons.add("Склад вернул ошибку " + e.status());
                    break;
                }
            }
        }

        if (businessException != null || isPendingConfirmation) {
            for (ReserveRequest reserveToRollback : successfulReserves) {
                try {
                    inventoryClient.unreserveStock(reserveToRollback);
                } catch (Exception rollbackEx) {
                }
            }
        }

        if (businessException != null) {
            throw businessException;
        }
        
        Order order = new Order();
        order.setCustomerName(request.customerName());
        order.setCustomerEmail(request.customerEmail());
        order.setCreatedAt(LocalDateTime.now());

        if (isPendingConfirmation) {
            order.setStatus("PENDING_CONFIRMATION");
            order.setStatusDetails("Требуется ручная проверка. Причины: " + String.join(", ", degradationReasons));
        } else {
            order.setStatus("CONFIRMED");
        }

        for (OrderItemRequest req : request.items()) {
            Item item = new Item();
            item.setProductId(req.productId());
            item.setQuantity(req.quantity());

            ProductDto product = productCache.get(req.productId());
            if (product != null) {
                item.setProductName(product.name());
                item.setPrice(product.price());
            } else {
                item.setProductName("Товар #" + req.productId() + " (ожидает проверки)");
                item.setPrice(BigDecimal.ZERO);
            }

            item.setOrder(order);
            order.getItems().add(item);
        }

        BigDecimal total = order.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(total);

        return orderMapper.toOrderDto(orderRepository.save(order));
    }
}
