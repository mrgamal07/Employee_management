package com.arun.Brocode.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DepartmentEntity {
    private Integer deptId;
    private String name;
    private String description;
}
