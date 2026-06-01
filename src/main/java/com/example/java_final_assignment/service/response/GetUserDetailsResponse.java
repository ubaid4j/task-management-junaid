package com.example.java_final_assignment.service.response;

import com.example.java_final_assignment.model.RoleEnum;
import com.example.java_final_assignment.model.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class GetUserDetailsResponse {
    private String username;
    private RoleEnum role;
    private String email;
    private StatusEnum status;
    private String createdAt;
}
