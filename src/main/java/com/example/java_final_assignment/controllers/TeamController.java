package com.example.java_final_assignment.controllers;

import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.AddMembersInTeamRequest;
import com.example.java_final_assignment.controllers.requests.CreateTeamRequest;
import com.example.java_final_assignment.controllers.requests.ViewAllTeamsRequest;
import com.example.java_final_assignment.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/*
 * RESTful Design Issues in This Controller:
 *
 * 1. SINGULAR RESOURCE NAME — @RequestMapping("team") should be @RequestMapping("/teams").
 *    REST treats resources as collections; the base path should be the plural noun "/teams".
 *
 * 2. VERBS IN URLs — REST URLs must be nouns only; HTTP methods convey the action.
 *    - "create-team"       → POST   /teams
 *    - "add-team-members"  → POST   /teams/{uuid}/members
 *    - "view-all-teams"    → GET    /teams
 *
 * 3. POST USED FOR READ OPERATION — "view-all-teams" fetches data but uses POST.
 *    Read operations must use GET so they are safe and cacheable (RFC 9110).
 *
 * 4. SUB-RESOURCE RELATIONSHIP NOT EXPRESSED — team members are a sub-resource of a team.
 *    The URL "/teams/{uuid}/members" makes the parent-child relationship explicit in the path,
 *    rather than encoding it inside a request body field (teamId).
 */
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("view-all-teams")
    public AppResponse viewAllTeams(@RequestBody ViewAllTeamsRequest request){
        return teamService.viewAllTeams(request);
    }
}
