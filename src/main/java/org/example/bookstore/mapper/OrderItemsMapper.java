package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.orderitemdto.OrderItemResponseDto;
import org.example.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemsMapper {
    @Mapping(target = "bookId",source = "book.id")
    OrderItemResponseDto toDto(OrderItem orderItem);

}
