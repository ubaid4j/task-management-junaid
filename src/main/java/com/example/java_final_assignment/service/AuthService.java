package com.example.java_final_assignment.service;

import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.controllers.requests.LoginRequestDTO;
import com.example.java_final_assignment.controllers.requests.RegisterRequestDTO;
import com.example.java_final_assignment.exceptions.GlobalException;
import com.example.java_final_assignment.model.RoleEnum;
import com.example.java_final_assignment.model.StatusEnum;
import com.example.java_final_assignment.model.User;
import com.example.java_final_assignment.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AppResponse registerUser(RegisterRequestDTO request){

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

        //accept the role from user
        //as only ADMIN can create the Users
        user.setRole(RoleEnum.USER);
        user.setStatus(StatusEnum.ACTIVE);

        //the user is proxy object (managed entity), we should avoid to return proxy object
        //user DTO pattern
        User savedUser = userRepository.save(user);

        return new AppResponse(savedUser);
    }

    public AppResponse login(LoginRequestDTO request){

        try{
            // This method authenticationManager.authenticate() triggers spring security authentication pipeline.
            //there is no need to add authentication and set it in SecurityContextHolder
            //as Spring Security Filters are responsible to do this out of the box

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            //here get user from database
            //check if user exists
            //check if password hashes matches 
            //then generate the token            

            String token = jwtService.generateToken(request.getEmail());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return new AppResponse(response);
        }
        catch(BadCredentialsException e){
            throw new GlobalException(401, "Invalid Email or Password");
        }

    }
}
