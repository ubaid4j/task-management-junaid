package com.example.java_final_assignment.auth.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    private String username;
    private String email;
    private String password;
}
