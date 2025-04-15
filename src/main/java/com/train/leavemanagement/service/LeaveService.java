package com.train.leavemanagement.service;

import com.train.leavemanagement.dto.CreateLeaveDTO;
import com.train.leavemanagement.dto.LeaveDTO;
import com.train.leavemanagement.dto.LeaveReportDTO;
import com.train.leavemanagement.entity.*;
import com.train.leavemanagement.repository.DepartmentMemberRepository;
import com.train.leavemanagement.repository.LeaveRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor

public class LeaveService {

    private final LeaveRepository leaveRepository;

    private final SecurityService securityService;
    private final DepartmentMemberRepository departmentMemberRepository;

    public List<LeaveDTO> getLeaveRequestByUser(){
        User user =securityService.getAuthenticatedUser();

        return leaveRepository.findByUser(user).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<LeaveReportDTO> getLeaveByRequestType(LeaveStatusType leaveStatusType){

        return leaveRepository.findByApplicationStatus(leaveStatusType).stream().map(this::statusReport).collect(Collectors.toList());
    }

    @Transactional
    public void editLeaveRequestByAdminOrManager(Long leaveId, LeaveStatusType leaveStatusType) {
        User user = securityService.getAuthenticatedUser();
        if (!user.getRole().equals(RoleType.ADMIN) && !user.getRole().equals(RoleType.MANAGER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allow to edit the leave request");
        }
        Leave leaveRequest = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave request not found"));

        User leaveRequester = leaveRequest.getUser();

        DepartmentMember personInChargeDepartmentMember = departmentMemberRepository.findByMember_Id(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "You are not in charge of any department"));

        DepartmentMember leaveRequesterDepartmentMember = departmentMemberRepository.findByMember_Id(leaveRequester.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave requester does not belong to any department"));

        if (!personInChargeDepartmentMember.getDepartment().getId().equals(leaveRequesterDepartmentMember.getDepartment().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only edit the leave requests of users in your same department");
        }

        leaveRequest.setApplicationStatus(leaveStatusType);
    }

    public void createLeaveRequest(CreateLeaveDTO createLeaveDTO){
        User user =securityService.getAuthenticatedUser();
        if(createLeaveDTO.getEndAt().isBefore(createLeaveDTO.getStartFrom())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leave end time cannot be before start time");
        }
        Leave leave = Leave.builder()
                .startFrom(createLeaveDTO.getStartFrom())
                .endAt(createLeaveDTO.getEndAt())
                .applicationStatus(LeaveStatusType.PENDING)
                .user(user)
                .build();
        leaveRepository.save(leave);
    }



    public LeaveDTO convertToDTO(Leave leave){
        return LeaveDTO.builder()
                .userId(leave.getUser().getId())
                .startFrom(leave.getStartFrom())
                .endAt(leave.getEndAt())
                .applicationStatus(leave.getApplicationStatus())
                .build();
    }

    public LeaveReportDTO statusReport(Leave leave){
        return LeaveReportDTO.builder()
                .userId(leave.getUser().getId())
                .userName(leave.getUser().getName())
                .startFrom(leave.getStartFrom())
                .endAt(leave.getEndAt())
                .build();
    }


}
