package com.example.java_final_assignment.filters;

import com.example.java_final_assignment.service.CustomUserDetailService;
import com.example.java_final_assignment.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Consider replacing this filter with spring-boot-starter-security-oauth2-resource-server.
// It handles JWT extraction, signature verification, and SecurityContext population automatically.
//
// The DB lookup per request (loadUserByUsername) is unnecessary — user validity
// (active, not restricted) should be enforced at login time only, when the token is issued.
// Once a signed token is in the user's hands, trusting the token's claims per request
// is the intended JWT model; re-querying the DB on every request defeats the stateless benefit of JWT.
//
// To migrate:
//   1. Configure a JwtDecoder bean with your signing key.
//   2. Add a JwtAuthenticationConverter to map your custom role claim to GrantedAuthorities.
//   3. Move all user-status checks (active, restricted) into the login/token-issuance flow.

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final CustomUserDetailService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        final String username = jwtService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);


                //===== FOR CHECKING =======
//                Authentication authentication =
//                        SecurityContextHolder.getContext().getAuthentication();
//
//                UserDetails userDetails1 =
//                        (UserDetails) authentication.getPrincipal();
//
//                System.out.println("YEH HAIN: "+userDetails1);
            }
        }

        filterChain.doFilter(request, response);
    }
}
