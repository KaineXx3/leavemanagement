package com.train.leavemanagement.repository;

import com.train.leavemanagement.entity.Department;
import com.train.leavemanagement.entity.DepartmentMember;
import com.train.leavemanagement.entity.DepartmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentMemberRepository extends JpaRepository<DepartmentMember, Long> {

    List<DepartmentMember> findAllByDepartment_Id(Long departmentId);

    Optional<DepartmentMember> findByDepartment_Id(Long departmentId);

    Optional<DepartmentMember> findByMember_Id(Long memberId);


}
