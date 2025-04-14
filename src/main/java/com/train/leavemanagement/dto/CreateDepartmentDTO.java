package com.train.leavemanagement.dto;

import com.train.leavemanagement.entity.DepartmentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDepartmentDTO {
    private DepartmentType departmentType;
    private String personInCharge;

}
