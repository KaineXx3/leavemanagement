package com.train.leavemanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "leaves")
public class Leave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startFrom;
    private LocalDate endAt;
    @Enumerated(EnumType.STRING)
    private LeaveStatusType applicationStatus;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
