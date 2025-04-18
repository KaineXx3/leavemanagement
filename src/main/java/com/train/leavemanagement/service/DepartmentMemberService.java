package com.train.leavemanagement.service;

import com.train.leavemanagement.dto.DepartmentMemberDTO;
import com.train.leavemanagement.entity.Department;
import com.train.leavemanagement.entity.DepartmentMember;
import com.train.leavemanagement.entity.User;
import com.train.leavemanagement.repository.DepartmentMemberRepository;
import com.train.leavemanagement.repository.DepartmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentMemberService {
    private final DepartmentMemberRepository departmentMemberRepository;
    private final SecurityService securityService;
    private final DepartmentRepository departmentRepository;

    @Cacheable(value = "departmentMembers", key = "#departmentId")
    public List<DepartmentMemberDTO> getAllMemberByDepartment(Long departmentId){
            return departmentMemberRepository.findAllByDepartment_Id(departmentId).stream().map(this::convertToDTO).collect(Collectors.toList());

    }

    @Cacheable(value = "departmentMemberCount", key = "#departmentId")
    public long countMembersInDepartment(Long departmentId){
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found");
        }
        return departmentMemberRepository.countByDepartment_Id(departmentId);    }


    @CacheEvict(value = {"departmentMembers", "departmentMemberCount"}, key = "#departmentId")
    public void joinDepartmentByUser(Long departmentId){
        User user = securityService.getAuthenticatedUser();

        if(departmentMemberRepository.findByMember_Id(user.getId()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are already belongs to one of the depart");
        }
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department Not found"));

        DepartmentMember departmentMember = DepartmentMember.builder().department(department).member(user).build();
        departmentMemberRepository.save(departmentMember);

    }


    public DepartmentMemberDTO convertToDTO(DepartmentMember departmentMember){
        return DepartmentMemberDTO.builder()
                .name(departmentMember.getMember().getName())
                .email(departmentMember.getMember().getEmail())
                .role(departmentMember.getMember().getRole())
                .build();
    }
}
