package com.example.java_final_assignment.controllers;

import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.AddMembersInTeamRequest;
import com.example.java_final_assignment.controllers.requests.CreateTeamRequest;
import com.example.java_final_assignment.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("create-team")
    public AppResponse createTeam(@RequestBody CreateTeamRequest request){
        return teamService.createTeam(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("add-team-members")
    public AppResponse addMembersInTeam(@RequestBody AddMembersInTeamRequest request){
        return teamService.addMembersInTeam(request);
    }
}
