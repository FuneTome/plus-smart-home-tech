package ru.yandex.practicum.order.dto.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.order.dto.CreateOrderRequest;
import ru.yandex.practicum.order.dto.OrderDto;
import ru.yandex.practicum.order.dto.OrderItemRequest;
import ru.yandex.practicum.order.entity.Item;
import ru.yandex.practicum.order.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order toOrder(CreateOrderRequest request);

    OrderDto toOrderDto(Order order);

    Item toItem(OrderItemRequest dto);
}
