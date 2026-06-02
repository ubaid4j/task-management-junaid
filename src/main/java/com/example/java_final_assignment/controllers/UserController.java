package com.example.java_final_assignment.controllers;

import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.CreateUserRequest;
import com.example.java_final_assignment.controllers.requests.GetAssignedUsersRequest;
import com.example.java_final_assignment.controllers.requests.GetUserDetailsRequest;
import com.example.java_final_assignment.controllers.requests.RestrictUserRequest;
import com.example.java_final_assignment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/*
 * RESTful Design Issues in This Controller:
 *
 * 1. SINGULAR RESOURCE NAME — @RequestMapping("user") should be @RequestMapping("/users").
 *    REST treats resources as collections; the base path should be the plural noun "/users".
 *
 * 2. VERBS IN URLs — REST URLs must be nouns only; HTTP methods convey the action.
 *    - "create-user"        → POST   /users
 *    - "restrict-user"      → PATCH  /users/{uuid}   (or PUT with full body)
 *    - "user-details"       → GET    /users/{uuid}
 *    - "get-assigned-users" → GET    /users?assignedTo={managerUuid}  (or /managers/{uuid}/users)
 *    - "get-personal-details" → GET  /users/me
 *
 * 3. POST USED FOR READ OPERATIONS — "user-details" and "get-assigned-users" fetch data
 *    but use POST. Read operations must use GET so they are safe and cacheable (RFC 9110).
 *
 * 4. REQUEST BODY FOR RESOURCE IDENTIFICATION — resources should be identified via path
 *    variables (e.g. /users/{uuid}), not inside a POST body, so the URL itself is addressable.
 */
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
    public AppResponse getUserDetails(@Valid @RequestBody GetUserDetailsRequest request){
        return userService.getUserDetails(request);
    }

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("get-assigned-users")
    public AppResponse getAssignedUsers(@RequestBody GetAssignedUsersRequest request){
        return userService.getAssignedUsers(request);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MANAGER') or hasRole('ADMIN')")
    @GetMapping("get-personal-details")
    public AppResponse getPersonalDetails(){
        return userService.getPersonalDetails();
    }

}
