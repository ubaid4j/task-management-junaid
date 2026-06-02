package com.example.java_final_assignment.service;

import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.*;
import com.example.java_final_assignment.exceptions.GlobalException;
import com.example.java_final_assignment.model.StatusEnum;
import com.example.java_final_assignment.model.Team;
import com.example.java_final_assignment.model.User;
import com.example.java_final_assignment.repositories.TeamMembersRepository;
import com.example.java_final_assignment.repositories.TeamRepository;
import com.example.java_final_assignment.repositories.UserRepository;
import com.example.java_final_assignment.service.response.CreateUserResponse;
import com.example.java_final_assignment.service.response.GetAssignedUsersResponse;
import com.example.java_final_assignment.service.response.GetUserDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamMembersRepository teamMembersRepository;


    public AppResponse createUser(CreateUserRequest request){
        Optional<User> alreadyExistUser = userRepository.findByEmail(request.getEmail());

        if(alreadyExistUser.isPresent())
            throw new GlobalException("Email already exists");

        User user = new User();

        user.setUsername(request.getUsername());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(request.getRole());
        user.setStatus(StatusEnum.ACTIVE);

        User savedUser = userRepository.save(user);


        return new AppResponse(
                new CreateUserResponse(
                        savedUser.getUuid(),
                        savedUser.getEmail(),
                        savedUser.getRole(),
                        savedUser.getStatus()
                )
        );
    }

    public AppResponse restrictUser(RestrictUserRequest request){

        Optional<User> user = userRepository.findByUuid(request.getUuid());

        if(user.isPresent()){
            user.get().setStatus(request.getStatus());
            userRepository.save(user.get());
            return new AppResponse("Status Updated");
        }

        throw new GlobalException("User not found");
    }

    public AppResponse getUserDetails(GetUserDetailsRequest request){

        Optional<User> user = userRepository.findByUuid(request.getUserId());

        if(user.isEmpty())
            throw new GlobalException("User not found");

        User existedUser = user.get();

        GetUserDetailsResponse response = new GetUserDetailsResponse();
        response.setStatus(existedUser.getStatus());
        response.setEmail(existedUser.getEmail());
        response.setRole(existedUser.getRole());
        response.setUsername(existedUser.getUsername());


        response.setCreatedAt(returnFormattedDate(existedUser.getCreatedAt()));

        return new AppResponse(response);
    }

    public AppResponse getAssignedUsers(GetAssignedUsersRequest request){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Optional<User> managerEntity = userRepository.findByEmail(email);

        if(managerEntity.isEmpty())
            throw new GlobalException("Manager not found");

        User manager = managerEntity.get();


        Optional<Team> teamEntity = teamRepository.findTeamByManagerUuid(manager.getUuid());

        if(teamEntity.isEmpty())
            throw new GlobalException("Manager is not linked to a team");

        Team team = teamEntity.get();


        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<GetAssignedUsersResponse> teamMembers =  teamMembersRepository.findAssignedUsersByTeamId(team.getId(), pageable);

        return new AppResponse(teamMembers.getContent());

    }


    public AppResponse getPersonalDetails(){

        User existedUser = getLoggedInUser();

        GetUserDetailsResponse response = new GetUserDetailsResponse();

        response.setStatus(existedUser.getStatus());
        response.setEmail(existedUser.getEmail());
        response.setRole(existedUser.getRole());
        response.setUsername(existedUser.getUsername());
        response.setCreatedAt(returnFormattedDate(existedUser.getCreatedAt()));

        return new AppResponse(response);
    }

    private String returnFormattedDate(LocalDateTime date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy");
        String formattedDate = date
                .toLocalDate()
                .format(formatter);

        return formattedDate;
    }

    private User getLoggedInUser(){
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Optional<User> userEntity = userRepository.findByEmail(email);

        if(userEntity.isEmpty())
            throw new GlobalException("User not found");

        return userEntity.get();
    }


}
