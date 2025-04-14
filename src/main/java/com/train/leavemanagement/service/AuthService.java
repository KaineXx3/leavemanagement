package com.train.leavemanagement.service;


import com.train.leavemanagement.dto.LoginRequestDTO;
import com.train.leavemanagement.dto.RegisterRequestDTO;
import com.train.leavemanagement.entity.User;
import com.train.leavemanagement.repository.UserRepository;
import com.train.leavemanagement.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public String register(RegisterRequestDTO requestDTO){
        if(userRepository.existsByEmail(requestDTO.getEmail())){
            throw new RuntimeException("Email already taken");
        }

        User user = User.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .role(requestDTO.getRole())
                .build();

        userRepository.save(user);
        return "Registration Successfully";
    }

    public String login(LoginRequestDTO loginRequestDTO){
        try {
            // Instead of calling authenticate, log the request and manually check for user
            User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            // Generate JWT token manually after verifying the user
            return jwtUtil.generateToken(loginRequestDTO.getEmail());

        } catch (Exception e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }



}
