package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.userdto.UserDto;
import org.example.bookstore.dto.userdto.UserRequestDto;
import org.example.bookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(User user);

    User toModel(UserRequestDto userRequestDto);
}
