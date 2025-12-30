package com.arun.Brocode.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class EmployeeEntity {
    private Integer sn;
    private String employeeCode;
    private String name;
    private String gender;
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date joinDate;
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date resignationDate;
    private Double salary;
    private String address;
    private String phone;
    private String email;
    private String education;
    private String designation;
    private String designationType; // FULL_TIME, PART_TIME, INTERN
    private Integer departmentId;
    private String departmentName;
    private String company;
    private String shiftType; // MORNING, EVENING, NIGHT, ROTATIONAL
    private String photo; // path to uploaded photo
    private String presentStatus;

}
