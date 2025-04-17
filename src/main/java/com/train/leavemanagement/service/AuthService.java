package com.train.leavemanagement.service;

import com.train.leavemanagement.dto.LoginRequestDTO;
import com.train.leavemanagement.dto.RegisterRequestDTO;
import com.train.leavemanagement.entity.User;
import com.train.leavemanagement.repository.UserRepository;
import com.train.leavemanagement.util.JwtUtil;
import com.train.leavemanagement.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SecurityService securityService;
    private final RedisTemplate<String, String> redisTemplate;

    // Register User

    public void register(RegisterRequestDTO requestDTO) {
        // Create a temporary User object for validation
        User tempUser = User.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .password(requestDTO.getPassword()) // Don't encode yet
                .build();

        // 🔍 Validate input
        ValidationUtil.validateUserRegistration(tempUser);

        // 💥 Check if email is taken AFTER validation
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("Email already taken");
        }

        // ✅ Build actual user object with encoded password
        User user = User.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .role(requestDTO.getRole())
                .build();

        userRepository.save(user);
    }


    // Login User
    public String login(LoginRequestDTO loginRequestDTO) {
        try {
            // Step 1: Check if the user exists
            User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Step 2: Verify password
            if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            // Step 3: Check Redis if a token is already stored for this user
            String tokenKey = "USER_TOKEN::" + loginRequestDTO.getEmail();
            String existingToken = redisTemplate.opsForValue().get(tokenKey);

            // Step 4: If token exists in cache, return it
            if (existingToken != null) {
                return existingToken;
            }

            // Step 5: Generate new token
            String newToken = jwtUtil.generateToken(loginRequestDTO.getEmail());

            // Step 6: Save both the token → email and email → token mappings in Redis
            Duration expiration = Duration.ofMillis(jwtUtil.getExpiration());
            redisTemplate.opsForValue().set(tokenKey, newToken, expiration);  // Store email → token
            redisTemplate.opsForValue().set(newToken, loginRequestDTO.getEmail(), expiration);  // Store token → email

            return newToken;

        } catch (Exception e) {
            throw new RuntimeException("Authentication failed", e);
        }
    }

    // Logout User
    public void logout() {
        // 🔐 Step 1: Extract token from Authorization header
        String token = securityService.getAuthToken();

        if (token == null) {
            throw new RuntimeException("No token provided in the Authorization header.");
        }

        // 🔄 Step 2: Look up the email associated with the token
        String email = redisTemplate.opsForValue().get(token);

        if (email != null) {
            // 🧹 Step 3: Build the Redis key used to store the user-token mapping
            String userKey = "USER_TOKEN::" + email;

            // ❌ Delete both directions of the mapping
            redisTemplate.delete(userKey);   // Remove email → token
            redisTemplate.delete(token);     // Remove token → email

            System.out.println("✅ Logged out: Token and user-token mapping removed from Redis");
        } else {
            System.out.println("⚠ Token not found in Redis. Maybe it's already expired or was never stored.");
        }
    }

}
