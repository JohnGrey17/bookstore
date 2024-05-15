package org.example.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bookstore.dto.userdto.UserLoginRequestDto;
import org.example.bookstore.dto.userdto.UserLoginResponseDto;
import org.example.bookstore.dto.userdto.UserRegistrationRequestDto;
import org.example.bookstore.dto.userdto.UserResponseDto;
import org.example.bookstore.exception.RegistrationException;
import org.example.bookstore.security.AuthenticationService;
import org.example.bookstore.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication Controller",description = "This controller handles request "
        + "and response with registration and login")
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "registration new  user",description = "registration new user "
            + " added them role for possible future act")
    @PostMapping("/registration")
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @Operation(summary = "Login User",
            description = "Authenticates a user by their credentials and generates an access token "
                    + "for further interactions with protected endpoints. "
                    + "The provided credentials should include the user's username "
                    + "and password in the request body.")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
