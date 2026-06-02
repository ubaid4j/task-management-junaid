package com.example.java_final_assignment.controllers.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class GetUserDetailsRequest {
    @NotNull(message = "User Id is required")
    private UUID userId;
}
