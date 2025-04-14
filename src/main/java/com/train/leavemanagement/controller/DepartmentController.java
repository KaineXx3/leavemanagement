package com.train.leavemanagement.controller;

import com.train.leavemanagement.dto.DepartmentDTO;
import com.train.leavemanagement.entity.DepartmentType;
import com.train.leavemanagement.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/department")
@Tag(name = "Department API", description = "Endpoints for managing departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "Get all departments", description = "Returns a list of all departments with their details.")
    @GetMapping("")
    public ResponseEntity<List<DepartmentDTO>> getDepartmentInformation() {
        try {
            return ResponseEntity.ok(departmentService.getDepartmentInformation());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @Operation(summary = "Create a department", description = "Allows an admin to create a new department by providing a department type.")
    @PostMapping("")
    public ResponseEntity<?> createDepartmentByAdmin(
            @Parameter(description = "The type of department to be created", required = true)
            @RequestParam DepartmentType departmentType) {
        try {
            departmentService.createDepartmentByAdmin(departmentType);
            return ResponseEntity.ok("Department created successfully");
        } catch (RuntimeException e) {
            if (e instanceof ResponseStatusException ex) {
                return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @Operation(summary = "Edit a department", description = "Allows an admin to update the department type of a specific department using its ID.")
    @PutMapping("")
    public ResponseEntity<?> editDepartmentByAdmin(
            @Parameter(description = "The ID of the department to be edited", required = true)
            @RequestParam Long departmentId,
            @Parameter(description = "The new department type to assign", required = true)
            @RequestParam DepartmentType departmentType) {
        try {
            departmentService.editDepartmentByAdmin(departmentId, departmentType);
            return ResponseEntity.ok("Department edited successfully");
        } catch (RuntimeException e) {
            if (e instanceof ResponseStatusException ex) {
                return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @Operation(summary = "Delete departments", description = "Deletes all departments. Intended for admin use.")
    @DeleteMapping("")
    public ResponseEntity<?> deleteDepartmentByAdmin() {
        try {
            departmentService.deleteDepartmentByAdmin();
            return ResponseEntity.ok("Department deleted successfully");
        } catch (RuntimeException e) {
            if (e instanceof ResponseStatusException ex) {
                return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
