package com.train.leavemanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "department_members")
public class DepartmentMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", unique = true, nullable = false)
    private User member;
}
