package org.example.bookstore.service.order;

import org.example.bookstore.dto.orderDto.OrderRequestDto;
import org.example.bookstore.dto.orderDto.OrderResponseDto;

public interface OrderService {
    OrderResponseDto createNewOrder(OrderRequestDto requestDto);
}
