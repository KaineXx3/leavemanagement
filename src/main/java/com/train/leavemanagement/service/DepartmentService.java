package com.train.leavemanagement.service;

import com.train.leavemanagement.dto.DepartmentDTO;
import com.train.leavemanagement.entity.Department;
import com.train.leavemanagement.entity.DepartmentType;
import com.train.leavemanagement.entity.RoleType;
import com.train.leavemanagement.entity.User;
import com.train.leavemanagement.repository.DepartmentMemberRepository;
import com.train.leavemanagement.repository.DepartmentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final SecurityService securityService;

    private final DepartmentMemberService departmentMemberService;



    @CachePut(value = "departments", key = "'all_departments'")
    public void createDepartmentByAdmin(DepartmentType departmentType){
        User user = securityService.getAuthenticatedUser();
        if(!user.getRole().equals(RoleType.ADMIN)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allow to create the department");
        }
        boolean exists = departmentRepository.existsByDepartmentType(departmentType);
        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department type already exists");
        }

        if (departmentRepository.findByPersonInCharge(user).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is already in charge of a department");
        }



        Department department = Department.builder()
                .departmentType(departmentType)
                .personInCharge(user)
                .build();


        departmentRepository.save(department);
        departmentMemberService.joinDepartmentByUser(user.getId());

    }

    @Transactional
    @CachePut(value = "departments", key = "'all_departments'")
    public void editDepartmentByAdmin(Long departmentId, DepartmentType departmentType){
        User user = securityService.getAuthenticatedUser();
        if(!user.getRole().equals(RoleType.ADMIN)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allow to create the department");
        }

        if(departmentRepository.existsByDepartmentType(departmentType)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This department already in charge of other person");
        }

        Department existingDepartment = departmentRepository.findById(departmentId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "No department found"));
        existingDepartment.setDepartmentType(departmentType);

    }

    @CacheEvict(value = "departments", key = "'all_departments'")
    public void deleteDepartmentByAdmin(){
        User user = securityService.getAuthenticatedUser();
        if(!user.getRole().equals(RoleType.ADMIN)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allow to create the department");
        }
        Department existingDepartment = departmentRepository.findByPersonInCharge(user).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "You don't have any department to delete"));
        departmentRepository.delete(existingDepartment);

    }



    @Cacheable(value = "departments", key = "'all_departments'")
    public List<DepartmentDTO> getDepartmentInformation(){
        return departmentRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DepartmentDTO convertToDTO(Department department){
        return DepartmentDTO.builder()
                .departmentType(department.getDepartmentType())
                .personInCharge(department.getPersonInCharge().getName())
                .email(department.getPersonInCharge().getEmail())
                .build();

    }
}
