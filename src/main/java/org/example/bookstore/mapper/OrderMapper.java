package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.orderDto.OrderRequestDto;
import org.example.bookstore.dto.orderDto.OrderResponseDto;
import org.example.bookstore.model.order.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
   OrderResponseDto toDto (Order order);
   Order toModel(OrderRequestDto requestDto);
}
