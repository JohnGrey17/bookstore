package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Tag(name = "Order Management", description = "Do manipulation with orders Api")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Create a new order", description = "Create a new order for "
            + "the authenticated user.")
    public OrderResponseDto createNewOrder(Authentication authentication,
                                           @RequestBody @Valid OrderRequestDto requestDto) {
        return orderService.createNewOrder(requestDto, authentication);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get user's order history", description = "Retrieve the order history"
            + " of the authenticated user.")
    public List<OrderResponseDto> getUsersOrdersHistory(Pageable pageable) {
        return orderService.getAllUserOrders(pageable);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get all items by order ID", description = "Retrieve all items associated "
            + "with a specific order ID.")
    public List<OrderItemResponseDto> getAllOrderItemByOrderId(@PathVariable Long orderId) {
        return orderService.getAllItemsByOrderId(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Operation(summary = "Get specified item by order ID and item ID", description =
            "Retrieve details " + "of a specific item by order ID and item ID.")
    public OrderItemResponseDto getSpecifiedItemInfo(@PathVariable Long orderId,
                                                     @PathVariable Long itemId) {
        return orderService.getItemByOrderIdAndItemId(orderId, itemId);
    }

    @PatchMapping(value = "/{orderId}")
    @Operation(summary = "Change order status", description = "Change the status"
            + " of an order by its ID.")
    public OrderUpdatedDto changeStatusOfOrder(@PathVariable Long orderId,
                                               @RequestBody OrderStatusUpdateRequest request) {
        return orderService.changeStatusOfOrderById(orderId,request);
    }
}
