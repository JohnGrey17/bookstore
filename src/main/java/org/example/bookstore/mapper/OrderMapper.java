package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.orderDto.OrderRequestDto;
import org.example.bookstore.dto.orderDto.OrderResponseDto;
import org.example.bookstore.model.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(config = MapperConfig.class,uses = {UserMapper.class, BookMapper.class,OrderItemsMapper.class} )
public interface OrderMapper {

   @Mapping(target = "userId", source = "user.id")
   OrderResponseDto toDto (Order order);
   Order toModel(OrderRequestDto requestDto);
}
