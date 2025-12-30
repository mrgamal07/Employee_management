package com.arun.Brocode.model;

import lombok.Data;

@Data
public class UserEntity {
    private int id;
    private String username;
    private String password;
    private Role role;
    private Integer employeeSn;
    private Status status;
}
