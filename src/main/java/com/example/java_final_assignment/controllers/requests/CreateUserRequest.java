package com.example.java_final_assignment.controllers.requests;

import com.example.java_final_assignment.model.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {

    @NotNull(message = "Username can not be null")
    @NotBlank(message = "Username can not be blank")
    private String username;

    @NotNull(message = "Email can not be null")
    @NotBlank(message = "Email can not be blank")
    private String email;

    @NotNull(message = "Password can not be null")
    @NotBlank(message = "Password can not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotNull(message = "Role can not be null")
    private RoleEnum role;
}
