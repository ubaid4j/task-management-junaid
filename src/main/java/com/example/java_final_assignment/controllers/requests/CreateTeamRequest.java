package com.example.java_final_assignment.controllers.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateTeamRequest {
    @NotNull(message = "Manager Id is required")
    private UUID managerId;

    @NotNull(message = "Team name is required")
    @NotBlank(message = "Team name is required")
    private String teamName;
}
