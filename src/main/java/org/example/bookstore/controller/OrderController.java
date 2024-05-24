package org.example.bookstore.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.orderdto.OrderRequestDto;
import org.example.bookstore.dto.orderdto.OrderResponseDto;
import org.example.bookstore.dto.orderdto.OrderStatusUpdateRequest;
import org.example.bookstore.dto.orderdto.OrderUpdatedDto;
import org.example.bookstore.dto.orderitemdto.OrderItemResponseDto;
import org.example.bookstore.service.order.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<OrderResponseDto> getUsersOrdersHistory(Pageable pageable) {
        return orderService.getAllUserOrders(pageable);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<OrderItemResponseDto> getAllOrderItemByOrderId(@PathVariable Long orderId) {
        return orderService.getAllItemsByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public OrderItemResponseDto getSpecifiedItemInfo(@PathVariable Long orderId,
                                                     @PathVariable Long itemId) {
        return orderService.getItemByOrderIdAndItemId(orderId, itemId);
    }

    @PutMapping("/{orderId}")
    public OrderUpdatedDto changeStatusOfOrder(@PathVariable Long orderId,
                                               @RequestBody OrderStatusUpdateRequest request) {
        return orderService.changeStatusOfOrderById(orderId,request);
    }
}
