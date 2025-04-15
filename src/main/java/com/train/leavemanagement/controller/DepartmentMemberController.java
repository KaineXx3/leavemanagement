package com.train.leavemanagement.controller;

import com.train.leavemanagement.dto.DepartmentMemberDTO;
import com.train.leavemanagement.service.DepartmentMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/department-members")
@Tag(name = "Department Member API", description = "Endpoints for managing department members")
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class DepartmentMemberController {

    private final DepartmentMemberService departmentMemberService;

    @Operation(
            summary = "Get all members by department ID",
            description = "Retrieve a list of all users who are members of a specific department."
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved department members")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{departmentId}")
    public ResponseEntity<List<DepartmentMemberDTO>> getAllMemberByDepartment(
            @Parameter(description = "ID of the department to retrieve members for", required = true)
            @PathVariable Long departmentId
    ) {
        try {
            return ResponseEntity.ok(departmentMemberService.getAllMemberByDepartment(departmentId));
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @Operation(
            summary = "Join a department",
            description = "Allows the authenticated user to join a department by department ID"
    )
    @ApiResponse(responseCode = "200", description = "Joined the department successfully")
    @ApiResponse(responseCode = "400", description = "User already belongs to another department")
    @ApiResponse(responseCode = "404", description = "Department not found")
    @ApiResponse(responseCode = "500", description = "Unexpected server error")
    @PostMapping("/")
    public ResponseEntity<?> joinDepartmentByUser(
            @Parameter(description = "ID of the department to join", required = true)
            @RequestParam Long departmentId
    ) {
        try {
            departmentMemberService.joinDepartmentByUser(departmentId);
            return ResponseEntity.ok("Joined the department successfully");
        } catch (RuntimeException e) {
            if (e instanceof ResponseStatusException ex) {
                return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
