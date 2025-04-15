package com.train.leavemanagement.dto;

import com.train.leavemanagement.entity.LeaveStatusType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateLeaveDTO {
    private LocalDate startFrom;
    private LocalDate endAt;

}
