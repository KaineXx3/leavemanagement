package com.train.leavemanagement.repository;

import com.train.leavemanagement.entity.Department;
import com.train.leavemanagement.entity.DepartmentType;
import com.train.leavemanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findById(Long id);
    Optional<Department> findByPersonInCharge(User personInCharge);

    boolean existsByDepartmentType(DepartmentType departmentType);

}
