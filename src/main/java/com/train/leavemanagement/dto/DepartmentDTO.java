package com.train.leavemanagement.dto;

import com.train.leavemanagement.entity.DepartmentType;
import com.train.leavemanagement.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
    private DepartmentType departmentType;
    private String personInCharge;

    private String email;
}
