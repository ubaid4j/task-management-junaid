package com.example.java_final_assignment.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class TeamMembersAddedResponse {
    private Integer addedCount;
    private Integer skippedCount;
}
