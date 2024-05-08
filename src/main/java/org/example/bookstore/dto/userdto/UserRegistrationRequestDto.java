package org.example.bookstore.dto.userdto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
    @AssertTrue(message = "Password and repeat password must match")
    public boolean isPasswordMatch() {
        return password.equals(repeatPassword);
    }
}
