package com.example.java_final_assignment.service;

import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.CreateUserRequest;
import com.example.java_final_assignment.controllers.requests.GetAssignedUsersResponse;
import com.example.java_final_assignment.controllers.requests.GetUserDetailsRequest;
import com.example.java_final_assignment.controllers.requests.RestrictUserRequest;
import com.example.java_final_assignment.exceptions.GlobalException;
import com.example.java_final_assignment.model.RoleEnum;
import com.example.java_final_assignment.model.StatusEnum;
import com.example.java_final_assignment.model.Team;
import com.example.java_final_assignment.model.User;
import com.example.java_final_assignment.repositories.TeamMembersRepository;
import com.example.java_final_assignment.repositories.TeamRepository;
import com.example.java_final_assignment.repositories.UserRepository;
import com.example.java_final_assignment.service.response.GetUserDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
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

        return new AppResponse(savedUser);
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy");
        String formattedDate = existedUser.getCreatedAt()
                .toLocalDate()
                .format(formatter);
        response.setCreatedAt(formattedDate);

        return new AppResponse(response);
    }

    public AppResponse getAssignedUsers(){
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


        List<GetAssignedUsersResponse> teamMembers =  teamMembersRepository.findAssignedUsersByTeamId(team.getId());

        return new AppResponse(teamMembers);

    }
}
