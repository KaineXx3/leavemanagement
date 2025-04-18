package com.train.leavemanagement.dto;

import com.train.leavemanagement.entity.LeaveReasonType;
import com.train.leavemanagement.entity.LeaveStatusType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LeaveReportDTO {
    private Long userId;
    private String userName;
    private LeaveReasonType leaveReasonType;

    private LocalDate startFrom;
    private LocalDate endAt;
}

