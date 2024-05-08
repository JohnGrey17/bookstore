package org.example.bookstore.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.userdto.UserRegistrationRequestDto;
import org.example.bookstore.dto.userdto.UserResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    @PostMapping("/registration")
    public UserResponseDto registerUser(@RequestBody UserRegistrationRequestDto requestDto) {
        return null;
    }
}
