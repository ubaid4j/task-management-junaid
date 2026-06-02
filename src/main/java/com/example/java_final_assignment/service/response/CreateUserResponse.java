package com.example.java_final_assignment.service.response;

import com.example.java_final_assignment.model.RoleEnum;
import com.example.java_final_assignment.model.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateUserResponse {

    private UUID userId;
    private String email;
    private RoleEnum role;
    private StatusEnum status;
}
