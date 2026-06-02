package com.example.java_final_assignment.service.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class ViewAllTeamsResponse {
    private UUID teamId;
    private String teamName;
    private UUID managerId;
    private String managerName;
}
