package com.example.java_final_assignment.controllers.requests;

import com.example.java_final_assignment.model.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class GetAssignedUsersResponse {
    private UUID userId;
    private String username;
    private String email;
    private StatusEnum status;
}
