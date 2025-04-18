package com.train.leavemanagement.dto;

import com.train.leavemanagement.entity.LeaveReasonType;
import com.train.leavemanagement.entity.LeaveStatusType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CreateLeaveDTO {
    @Schema(description = "Type of leave", example = "VACATION")

    private LeaveReasonType leaveReasonType;

    private LocalDate startFrom;
    private LocalDate endAt;

}
