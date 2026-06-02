package com.example.java_final_assignment.controllers.requests;

import com.example.java_final_assignment.model.StatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RestrictUserRequest {
    @NotNull(message = "User uuid can not be null")
    private UUID uuid;

    @NotNull(message = "Status can not be null")
    private StatusEnum status;
}
