package org.example.bookstore.dto.userdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(
        @NotBlank
        @Email
        @Length(min = 6, max = 254)
        String email,
        @NotBlank
        @Length(min = 4,max = 32)
        String password
) {

}
