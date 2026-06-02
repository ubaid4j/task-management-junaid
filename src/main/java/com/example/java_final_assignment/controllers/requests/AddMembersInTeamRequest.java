package com.example.java_final_assignment.controllers.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class AddMembersInTeamRequest {

    @NotNull(message = "TeamId cannot be null")
    private UUID teamId;
    List<UUID> userIds;
}
