package com.train.leavemanagement.controller;

import com.train.leavemanagement.dto.CreateLeaveDTO;
import com.train.leavemanagement.dto.LeaveDTO;
import com.train.leavemanagement.dto.LeaveReportDTO;
import com.train.leavemanagement.entity.LeaveStatusType;
import com.train.leavemanagement.service.LeaveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/leave")
@Tag(name = "Leave API", description = "Endpoints for managing leaves")
@AllArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class LeaveController {

    private final LeaveService leaveService;

    @Operation(summary = "Get all leave requests for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved leave requests"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("")
    public ResponseEntity<List<LeaveDTO>> getLeaveRequestByUser() {
        try {
            return ResponseEntity.ok(leaveService.getLeaveRequestByUser());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @Operation(summary = "Admin or Manager get leave requests by status type (e.g. PENDING, APPROVED, REJECTED)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved leave requests"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @GetMapping("/report/requestType")
    public ResponseEntity<List<LeaveReportDTO>> getLeaveRequestType(
            @Parameter(description = "Leave status type") @RequestParam LeaveStatusType leaveStatusType) {
        try {
            return ResponseEntity.ok(leaveService.getLeaveByRequestType(leaveStatusType));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @Operation(summary = "Approve or reject a leave request by Admin/Manager")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated leave request"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Leave request not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("")
    public ResponseEntity<?> editLeaveRequestByAdminOrManager(
            @Parameter(description = "ID of the leave request to be updated") @RequestParam Long leaveId,
            @Parameter(description = "New leave status") @RequestParam LeaveStatusType leaveStatusType) {
        try {
            leaveService.editLeaveRequestByAdminOrManager(leaveId, leaveStatusType);
            return ResponseEntity.ok("Updated Successfully");
        } catch (RuntimeException e) {
            if (e instanceof ResponseStatusException ex) {
                return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    @Operation(summary = "Create a new leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created leave request"),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("")
    public ResponseEntity<?> createLeaveRequest(
            @Parameter(description = "Leave request details") @RequestBody CreateLeaveDTO createLeaveDTO) {
        try {
            leaveService.createLeaveRequest(createLeaveDTO);
            return ResponseEntity.ok("Created Successfully");
        } catch (RuntimeException e) {
            if (e instanceof ResponseStatusException ex) {
                return ResponseEntity.status(ex.getStatusCode()).body(ex.getReason());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}
