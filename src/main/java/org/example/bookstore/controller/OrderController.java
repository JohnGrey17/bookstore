package org.example.bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.orderDto.OrderRequestDto;
import org.example.bookstore.dto.orderDto.OrderResponseDto;
import org.example.bookstore.service.order.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public OrderResponseDto createNewOrder(Authentication authentication,
                                           @RequestBody OrderRequestDto requestDto) {
        return orderService.createNewOrder(requestDto, authentication);
    }
}
