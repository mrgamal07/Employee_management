package com.arun.Brocode.Repository;

import com.arun.Brocode.model.Role;
import com.arun.Brocode.model.Status;
import com.arun.Brocode.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // sinup user
    public int saveUserEntity(UserEntity userEntity) {
        String sql = "INSERT INTO users(username,password,role,employee_sn,status) VALUES(?,?,?,?,?)";
        return jdbcTemplate.update(sql, userEntity.getUsername(), userEntity.getPassword(), userEntity.getRole().name(),
                userEntity.getEmployeeSn(), userEntity.getStatus().name());
    }

    // Login method
    public UserEntity findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND status = 'ACTIVE'";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                UserEntity u = new UserEntity();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                u.setRole(Role.valueOf(rs.getString("role")));
                u.setStatus(Status.valueOf(rs.getString("status")));
                u.setEmployeeSn(rs.getInt("employee_sn"));
                return u;
            }, username, password); // ✅ arguments at the end
        } catch (Exception e) {
            return null; // login failed
        }
    }

    // Fetch all employees for dropdown in signup page
    public List<Map<String, Object>> findAllEmployees() {
        String sql = "SELECT sn, name, employee_code FROM employee";
        return jdbcTemplate.queryForList(sql);
    }

}
