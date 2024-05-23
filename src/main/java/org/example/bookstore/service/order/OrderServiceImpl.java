package org.example.bookstore.service.order;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.orderDto.OrderRequestDto;
import org.example.bookstore.dto.orderDto.OrderResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    @Override
    public OrderResponseDto createNewOrder(OrderRequestDto requestDto) {
        return null;
    }
}
