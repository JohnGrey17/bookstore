package org.example.bookstore.service.order;

import java.util.List;
import org.example.bookstore.dto.orderdto.OrderRequestDto;
import org.example.bookstore.dto.orderdto.OrderResponseDto;
import org.example.bookstore.dto.orderdto.OrderStatusUpdateRequest;
import org.example.bookstore.dto.orderdto.OrderUpdatedDto;
import org.example.bookstore.dto.orderitemdto.OrderItemResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderResponseDto createNewOrder(OrderRequestDto requestDto,
                                    Authentication authentication);

    List<OrderResponseDto> getAllUserOrders(Pageable pageable);

    List<OrderItemResponseDto> getAllItemsByOrderId(Long orderId);

    OrderItemResponseDto getItemByOrderIdAndItemId(Long orderId, Long itemId);

    OrderUpdatedDto changeStatusOfOrderById(Long orderId, OrderStatusUpdateRequest request);
}
