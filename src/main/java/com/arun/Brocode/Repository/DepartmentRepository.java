package com.arun.Brocode.Repository;

import com.arun.Brocode.model.DepartmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartmentRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // add department
    public int insert(DepartmentEntity dept) {
        String sql = "INSERT INTO departments(name,description) VALUES(?,?)";
        return jdbcTemplate.update(sql, dept.getName(), dept.getDescription());
    }

    // get all departments
    public List<DepartmentEntity> findAll() {
        String sql = "SELECT * FROM departments";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            DepartmentEntity d = new DepartmentEntity();
            d.setDeptId(rs.getInt("dept_id"));
            d.setName(rs.getString("name"));
            d.setDescription(rs.getString("description"));
            return d;
        });
    }

    // Get department by id
    public DepartmentEntity findById(int id) {
        String sql = "SELECT * FROM departments WHERE dept_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            DepartmentEntity d = new DepartmentEntity();
            d.setDeptId(rs.getInt("dept_id"));
            d.setName(rs.getString("name"));
            d.setDescription(rs.getString("description"));
            return d;
        }, id);
    }

    // Update department
    public int update(DepartmentEntity dept) {
        String sql = "UPDATE departments SET name=?, description=? WHERE dept_id=?";
        return jdbcTemplate.update(sql, dept.getName(), dept.getDescription(), dept.getDeptId());
    }

    // Delete department
    public int delete(int id) {
        String sql = "DELETE FROM departments WHERE dept_id=?";
        return jdbcTemplate.update(sql, id);
    }
}
