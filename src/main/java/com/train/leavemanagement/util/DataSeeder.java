package com.train.leavemanagement.util;

import com.train.leavemanagement.entity.*;
import com.train.leavemanagement.repository.DepartmentMemberRepository;
import com.train.leavemanagement.repository.DepartmentRepository;
import com.train.leavemanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;
    private final DepartmentMemberRepository departmentMemberRepository;

    private final Map<String, User> users = new HashMap<>();
    private final Map<DepartmentType, Department> departments = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            seedUsers();
            seedDepartments();
            seedDepartmentMembers();
            System.out.println("✔️Users, departments, and department members seeded successfully!");
            //System.out.println("\u2705 Users, departments, and department members seeded successfully!");

        }
    }

    private void seedUsers() {
        User admin1 = saveUser("Admin User", "admin@example.com", "admin123", RoleType.ADMIN);
        User admin2 = saveUser("Admin User", "admin2@example.com", "admin123", RoleType.ADMIN);
        User user1 = saveUser("Regular User", "user@example.com", "user123", RoleType.EMPLOYEE);
        User user2 = saveUser("Regular User", "user2@example.com", "user123", RoleType.EMPLOYEE);
        User manager1 = saveUser("Manager", "manager@example.com", "manager123", RoleType.MANAGER);
        User manager2 = saveUser("Manager", "manager2@example.com", "manager123", RoleType.MANAGER);
    }

    private User saveUser(String name, String email, String rawPassword, RoleType role) {
        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .build();
        User savedUser = userRepository.save(user);
        users.put(email, savedUser);
        return savedUser;
    }

    private void seedDepartments() {
        User admin1 = users.get("admin@example.com");
        User admin2 = users.get("admin2@example.com");

        Department accounting = saveDepartment(DepartmentType.ACCOUNTING, admin1);
        Department icdc = saveDepartment(DepartmentType.ICDC, admin2);
    }

    private Department saveDepartment(DepartmentType type, User admin) {
        Department department = Department.builder()
                .departmentType(type)
                .personInCharge(admin)
                .build();
        Department savedDepartment = departmentRepository.save(department);
        departments.put(type, savedDepartment);
        return savedDepartment;
    }

    private void seedDepartmentMembers() {
        saveMember("user@example.com", DepartmentType.ICDC);
        saveMember("user2@example.com", DepartmentType.ACCOUNTING);
        saveMember("manager@example.com", DepartmentType.ICDC);
        saveMember("manager2@example.com", DepartmentType.ACCOUNTING);
        saveMember("admin@example.com", DepartmentType.ICDC); // Admin also becomes member
        saveMember("admin2@example.com", DepartmentType.ACCOUNTING);
    }

    private void saveMember(String email, DepartmentType type) {
        User member = users.get(email);
        Department department = departments.get(type);

        DepartmentMember departmentMember = DepartmentMember.builder()
                .department(department)
                .member(member)
                .build();

        departmentMemberRepository.save(departmentMember);
    }
}
