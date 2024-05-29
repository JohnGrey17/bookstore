package org.example.bookstore.service.user;

import org.example.bookstore.dto.userdto.UserRegistrationRequestDto;
import org.example.bookstore.dto.userdto.UserResponseDto;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto request) throws RegistrationException;

    User getUserById(Long userId);
}
