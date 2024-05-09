package org.example.bookstore.dto.userdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.bookstore.validator.FieldMatch;
import org.hibernate.validator.constraints.Length;

@FieldMatch(first = "password",second = "repeatPassword",
        message = "Password and repeatPassword should match")
@Data
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Length(min = 4,max = 32)
    private String password;
    @NotBlank
    @Length(min = 4,max = 32)
    private String repeatPassword;
    private String firstName;
    private String lastName;
    private String shippingAddress;
}
