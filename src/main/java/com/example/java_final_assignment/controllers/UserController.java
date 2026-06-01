package com.example.java_final_assignment.controllers;

import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.CreateUserRequest;
import com.example.java_final_assignment.controllers.requests.GetUserDetailsRequest;
import com.example.java_final_assignment.controllers.requests.RestrictUserRequest;
import com.example.java_final_assignment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService userService;


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("create-user")
    public AppResponse createUser(@RequestBody CreateUserRequest request){
        return userService.createUser(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("restrict-user")
    public AppResponse restrictUser(@RequestBody RestrictUserRequest request){
        return userService.restrictUser(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("user-details")
    public AppResponse getUserDetails(@RequestBody GetUserDetailsRequest request){
        return userService.getUserDetails(request);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("get-assigned-users")
    public AppResponse getAssignedUsers(){
        return userService.getAssignedUsers();
    }

}
