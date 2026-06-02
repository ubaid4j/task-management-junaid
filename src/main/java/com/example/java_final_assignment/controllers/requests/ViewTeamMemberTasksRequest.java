package com.example.java_final_assignment.controllers.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ViewTeamMemberTasksRequest {

    @NotNull(message = "Assignee Id can not be null")
    private UUID assigneeId;

    // Defaults to page 0 if missing
    private Integer page = 0;

    // Defaults to 5 entries if missing
    private Integer size = 5;
}
