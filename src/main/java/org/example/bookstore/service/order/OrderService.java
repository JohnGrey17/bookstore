package org.example.bookstore.service.order;

import org.example.bookstore.dto.orderDto.OrderRequestDto;
import org.example.bookstore.dto.orderDto.OrderResponseDto;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderResponseDto createNewOrder(OrderRequestDto requestDto,
                                    Authentication authentication);
}
