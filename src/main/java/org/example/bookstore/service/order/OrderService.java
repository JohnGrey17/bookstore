package org.example.bookstore.service.order;

import java.util.List;
import org.example.bookstore.dto.orderdto.OrderRequestDto;
import org.example.bookstore.dto.orderdto.OrderResponseDto;
import org.example.bookstore.dto.orderdto.OrderStatusUpdateRequestDto;
import org.example.bookstore.dto.orderdto.OrderUpdatedDto;
import org.example.bookstore.dto.orderitemdto.OrderItemResponseDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createNewOrder(OrderRequestDto requestDto,
                                    Long userId);

    List<OrderResponseDto> getAllUserOrders(Long userid,Pageable pageable);

    List<OrderItemResponseDto> getAllItemsByOrderId(Long orderId);

    OrderItemResponseDto getItemByOrderIdAndItemId(Long orderId, Long itemId);

    OrderUpdatedDto changeStatusOfOrderById(Long orderId, OrderStatusUpdateRequestDto request);
}
