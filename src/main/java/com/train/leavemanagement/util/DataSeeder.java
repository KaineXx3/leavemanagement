package com.train.leavemanagement.util;

import com.train.leavemanagement.entity.RoleType;
import com.train.leavemanagement.entity.User;
import com.train.leavemanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @Override
    public void run(String... args) throws Exception {
        // Check if users already exist to prevent re-seeding
        if (userRepository.count() == 0) {
            // Creating some users with encoded passwords
            User admin1 = User.builder()
                    .name("Admin User")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123")) // Encode the password
                    .role(RoleType.ADMIN)
                    .build();

            User admin2 = User.builder()
                    .name("Admin User")
                    .email("admin2@example.com")
                    .password(passwordEncoder.encode("admin123")) // Encode the password
                    .role(RoleType.ADMIN)
                    .build();

            User user1 = User.builder()
                    .name("Regular User")
                    .email("user@example.com")
                    .password(passwordEncoder.encode("user123")) // Encode the password
                    .role(RoleType.EMPLOYEE)
                    .build();

            User user2 = User.builder()
                    .name("Regular User")
                    .email("user2@example.com")
                    .password(passwordEncoder.encode("user123")) // Encode the password
                    .role(RoleType.EMPLOYEE)
                    .build();

            User manager1 = User.builder()
                    .name("Manager")
                    .email("manager@example.com")
                    .password(passwordEncoder.encode("manager123")) // Encode the password
                    .role(RoleType.MANAGER)
                    .build();

            User manager2 = User.builder()
                    .name("Manager")
                    .email("manager2@example.com")
                    .password(passwordEncoder.encode("manager123")) // Encode the password
                    .role(RoleType.MANAGER)
                    .build();

            // Save them to the database
            userRepository.save(admin1);
            userRepository.save(admin2);
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(manager1);
            userRepository.save(manager2);

            System.out.println("Data seeded successfully!");
        }
    }
}
