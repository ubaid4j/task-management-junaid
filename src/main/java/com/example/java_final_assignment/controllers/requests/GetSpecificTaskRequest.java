package com.example.java_final_assignment.controllers.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class GetSpecificTaskRequest {

    @NotNull(message = "Task Id is required")
    private UUID taskId;
}
