package org.example.bookstore.mapper;

import org.example.bookstore.config.MapperConfig;
import org.example.bookstore.dto.userdto.UserRegistrationRequestDto;
import org.example.bookstore.dto.userdto.UserResponseDto;
import org.example.bookstore.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto bookRequestDto);

    UserResponseDto toDto(User user);
}
