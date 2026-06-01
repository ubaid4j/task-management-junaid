package com.example.java_final_assignment.controllers;

import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.LoginRequestDTO;
import com.example.java_final_assignment.controllers.requests.RegisterRequestDTO;
import com.example.java_final_assignment.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/hello")
    public String hello(){
        return "Hello Springboot";
    }


    @PostMapping("/register")
    public AppResponse register(
            @RequestBody RegisterRequestDTO request
    ) {
      return authService.registerUser(request);
    }


    @PostMapping("/login")
    public AppResponse login(@RequestBody LoginRequestDTO request){
        return authService.login(request);
    }
}
