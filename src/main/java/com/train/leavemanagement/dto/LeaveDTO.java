package com.train.leavemanagement.dto;

import com.train.leavemanagement.entity.LeaveReasonType;
import com.train.leavemanagement.entity.LeaveStatusType;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LeaveDTO {
    private Long userId;
    private LeaveReasonType leaveReasonType;


    private LocalDate startFrom;
    private LocalDate endAt;
    private LeaveStatusType applicationStatus;

}
