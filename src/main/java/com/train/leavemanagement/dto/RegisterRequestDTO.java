package com.train.leavemanagement.dto;

import com.train.leavemanagement.entity.DepartmentType;
import com.train.leavemanagement.entity.RoleType;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {
    private String name;

    private String password;

    private String email;
    private RoleType role;


}
