package com.train.leavemanagement.dto;

import com.train.leavemanagement.entity.Department;
import com.train.leavemanagement.entity.RoleType;
import com.train.leavemanagement.entity.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentMemberDTO {

    private String name;
    private String email;
    private RoleType role;
}
