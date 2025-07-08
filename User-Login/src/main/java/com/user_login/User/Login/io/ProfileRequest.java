package com.user_login.User.Login.io;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileRequest {
    @NotBlank(message = "Name should not be blank")
    private String name;
    @NotNull(message = "Name should not be blank")
    @Email(message = "Email address should be valid")
    private String email;
    @Size(min = 6,message = "Password should be atleast 6 characters")
    private String password;
}
