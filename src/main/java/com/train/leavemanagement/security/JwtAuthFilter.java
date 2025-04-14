package com.train.leavemanagement.security;


import com.train.leavemanagement.entity.User;
import com.train.leavemanagement.repository.UserRepository;
import com.train.leavemanagement.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@AllArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = extractTokenFromHeader(request);

        if (token != null) {
            String email = extractEmailFromRequest(request);

            if (jwtUtil.validateToken(token, email)) {
                // Find the actual User object
                User user = userRepository.findByEmail(email)
                        .orElse(null);

                if (user != null) {
                    // Set the User object as the principal
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user, null,  Collections.emptyList()); // Set user as principal
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    // Extract JWT token from the Authorization header
    private String extractTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // Extract email from the request's token
    private String extractEmailFromRequest(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        if (token != null) {
            // Use JwtUtil to extract the email from the token
            return jwtUtil.extractEmail(token);
        }
        return null;
    }
}
