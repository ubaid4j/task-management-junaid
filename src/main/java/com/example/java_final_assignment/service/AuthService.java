package com.example.java_final_assignment.service;

import com.example.java_final_assignment.GlobalResponse.AppResponse;
import com.example.java_final_assignment.auth.requests.LoginRequestDTO;
import com.example.java_final_assignment.auth.requests.RegisterRequestDTO;
import com.example.java_final_assignment.model.Role;
import com.example.java_final_assignment.model.User;
import com.example.java_final_assignment.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AppResponse registerUser(RegisterRequestDTO request){
        User user = new User();

        user.setUsername(request.getUsername());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        return new AppResponse(savedUser);
    }

    public AppResponse login(LoginRequestDTO request){

        try{
            // This method authenticationManager.authenticate() triggers spring security authentication pipeline.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            String token = jwtService.generateToken(authentication.getName());

            return new AppResponse(token);
        }
        catch(BadCredentialsException e){
            return new AppResponse(401, "Invalid Email or Password");
        }

    }
}
