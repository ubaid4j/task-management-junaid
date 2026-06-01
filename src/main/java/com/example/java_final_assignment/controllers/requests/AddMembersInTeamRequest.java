package com.example.java_final_assignment.controllers.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class AddMembersInTeamRequest {
    private UUID teamId;
    List<UUID> userIds;
}
