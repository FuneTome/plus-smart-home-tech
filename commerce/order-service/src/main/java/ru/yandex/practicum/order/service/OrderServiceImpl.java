package ru.yandex.practicum.order.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderItemRequest;
import ru.yandex.practicum.order.dto.mapper.OrderMapper;
import ru.yandex.practicum.order.entity.Item;
import ru.yandex.practicum.order.entity.Order;
import ru.yandex.practicum.order.exception.NotFoundException;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrder(long id) {
        return orderMapper.toOrderDto(orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заказ с id = " + id + " не найден")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getOrdersByEmail(String email) {
        //не проверяем существует ли почта тк нам впринципе не важно существует ли такая почта
        // да и есть ли на эту почту заказы
        return orderRepository.findAllByCustomerEmail(email).stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }

    @Override
    public OrderDto createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.customerName());
        order.setCustomerEmail(request.customerEmail());
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());

        for (OrderItemRequest req : request.items()) {
            Item item = new Item();
            item.setProductId(req.productId());
            item.setProductName(req.productName());
            item.setQuantity(req.quantity());
            item.setPrice(req.price());
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
