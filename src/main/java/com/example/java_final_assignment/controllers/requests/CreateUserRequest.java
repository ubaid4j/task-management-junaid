package com.example.java_final_assignment.controllers.requests;

import com.example.java_final_assignment.model.RoleEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String username;
    private String email;
    private String password;
    private RoleEnum role;
}
