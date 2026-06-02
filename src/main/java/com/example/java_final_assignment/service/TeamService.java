package com.example.java_final_assignment.service;


import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.AddMembersInTeamRequest;
import com.example.java_final_assignment.controllers.requests.CreateTeamRequest;
import com.example.java_final_assignment.controllers.requests.ViewAllTeamsRequest;
import com.example.java_final_assignment.exceptions.GlobalException;
import com.example.java_final_assignment.model.RoleEnum;
import com.example.java_final_assignment.model.Team;
import com.example.java_final_assignment.model.TeamMember;
import com.example.java_final_assignment.model.User;
import com.example.java_final_assignment.repositories.TeamMembersRepository;
import com.example.java_final_assignment.repositories.TeamRepository;
import com.example.java_final_assignment.repositories.UserRepository;
import com.example.java_final_assignment.service.response.NewTeamResponse;
import com.example.java_final_assignment.service.response.TeamMembersAddedResponse;
import com.example.java_final_assignment.service.response.ViewAllTeamsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamMembersRepository teamMembersRepository;

    public AppResponse createTeam (CreateTeamRequest request){
        Optional<User> manager = userRepository.findByUuidAndRole(request.getManagerId(), RoleEnum.MANAGER);
        if(manager.isPresent()){

            Optional<Team> team = teamRepository.findTeamByManagerUuid(manager.get().getUuid());

            if(team.isPresent())
                throw new GlobalException("Manager already manages a Team");

            Team newTeam = new Team();
            newTeam.setManager(manager.get());
            newTeam.setName(request.getTeamName());
            Team savedTeam = teamRepository.save(newTeam);

            return new AppResponse(new NewTeamResponse(
                    savedTeam.getUuid(),
                    savedTeam.getName()
            ));
        }

        throw new GlobalException("Manager not found");

    }

    public AppResponse addMembersInTeam(AddMembersInTeamRequest request){
        Optional<Team> team = teamRepository.findByUuid(request.getTeamId());

        if(team.isEmpty())
            throw new GlobalException("Team not found");

        Set<UUID> uniqueUserIds = new HashSet<>(request.getUserIds());

        List<User> users = userRepository.findByUuidIn(new ArrayList<>(uniqueUserIds));

        List<UUID> alreadyPartOfTeamUser = teamMembersRepository.findUserUuidsByTeamId(team.get().getId());
        //For performance, we made it a hashset to make our lookup fast
        Set<UUID> existingUserIds = new HashSet<>(alreadyPartOfTeamUser);

        Set<UUID> alreadyAssignedToOtherTeamsUser = new HashSet<>(
                teamMembersRepository.findUserUuidsAlreadyInAnyTeam(
                        new ArrayList<>(uniqueUserIds)
                )
        );

        List<User> validUsers = users.stream()
                .filter(user -> user.getRole() == RoleEnum.USER) //Role must be user
                .filter(user -> !existingUserIds.contains(user.getUuid()))  //Already in this team
                .filter(user -> !alreadyAssignedToOtherTeamsUser.contains(user.getUuid())) //Already assigned to other teams
                .toList();


        List<TeamMember> newMembers = validUsers.stream()
                .map(user -> {
                    TeamMember tm = new TeamMember();
                    tm.setTeam(team.get());
                    tm.setUser(user);
                    return tm;
                })
                .toList();

        if(newMembers.size()>0)
            teamMembersRepository.saveAll(newMembers);

        int skippedUsersCount = users.size() - newMembers.size();
        return new AppResponse(new TeamMembersAddedResponse(newMembers.size(), skippedUsersCount));

    }

    public AppResponse viewAllTeams(ViewAllTeamsRequest request){

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Team> teams = teamRepository.findAll(pageable);

        List<ViewAllTeamsResponse> response = new ArrayList<>();

        for(Team team: teams){
            ViewAllTeamsResponse res = new ViewAllTeamsResponse();

            res.setTeamId(team.getUuid());
            res.setTeamName(team.getName());
            res.setManagerId(team.getManager().getUuid());
            res.setManagerName(team.getManager().getUsername());

            response.add(res);
        }

        return new AppResponse(response);

    }
}
