package com.arun.Brocode.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class SalaryPayment {
    private Integer salaryId;
    private Integer employeeSn;
    private Integer salaryYear;
    private String salaryMonth; // ENUM('JAN', 'FEB', ...)
    private BigDecimal baseSalary;
    private BigDecimal bonus;
    private BigDecimal deduction;
    private BigDecimal netSalary;
    private Date paymentDate;
    private Integer paidBy; // Admin user ID
    private Date createdAt;

    // For displaying employee name in admin view
    private String employeeName;
    private String departmentName;
}
