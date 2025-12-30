package com.arun.Brocode.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class LeaveRequest {
    private Integer leaveId;
    private Integer employeeSn;
    private String leaveType; // ENUM('CASUAL','SICK','FESTIVAL')
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String reason;
    private String status; // ENUM('PENDING','APPROVED','REJECTED')
    private Date appliedAt;
    private Integer reviewedBy;
    private Date reviewedAt;
    private String adminRemarks;
}
