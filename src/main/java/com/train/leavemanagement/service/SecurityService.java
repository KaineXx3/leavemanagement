package com.train.leavemanagement.service;

import com.train.leavemanagement.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SecurityService {

    private final HttpServletRequest request;


    //get authenticated
    //For ex: User A only can crud the data of themselves instead of manually type the userid
    public User getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        if(authentication!=null && authentication.getPrincipal() instanceof User){
            return (User) authentication.getPrincipal();
        }
        throw new IllegalStateException("User not authenticated");
    }

    public String getAuthToken() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // strip "Bearer "
        }
        return null;
    }
}
