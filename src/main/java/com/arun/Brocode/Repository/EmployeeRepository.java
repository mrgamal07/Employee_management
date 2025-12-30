package com.arun.Brocode.Repository;

import com.arun.Brocode.model.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<EmployeeEntity> findPaginatedWithDepartment(int offset, int limit) {
        String sql = """
                    SELECT e.*, d.name AS department_name
                    FROM employee e
                    LEFT JOIN departments d ON e.department_id = d.dept_id
                    LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EmployeeEntity e = new EmployeeEntity();
            e.setSn(rs.getInt("sn"));
            e.setEmployeeCode(rs.getString("employee_code"));
            e.setName(rs.getString("name"));
            e.setGender(rs.getString("gender"));
            e.setDesignation(rs.getString("designation"));
            e.setPresentStatus(rs.getString("present_status"));
            e.setDepartmentId(rs.getInt("department_id")); // important
            e.setDepartmentName(rs.getString("department_name")); // if you have this field in EmployeeEntity
            return e;
        }, limit, offset);
    }

    public int countAllEmployees() {
        String sql = "SELECT COUNT(*) FROM employee";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    // Insert employee
    public int insert(EmployeeEntity employee) {
        String sql = """

                INSERT INTO employee
                (employee_code, name, gender, dob, join_date, resignation_date, salary, address, phone, email, education, designation, designation_type, department_id, company, shift_type, photo, present_status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        System.out.println("Repository Inserting Employee. Dept ID: " + employee.getDepartmentId()); // Debugging
        return jdbcTemplate.update(sql,
                employee.getEmployeeCode(),
                employee.getName(),
                employee.getGender(),
                employee.getDob(),
                employee.getJoinDate(),
                employee.getResignationDate(),
                employee.getSalary(),
                employee.getAddress(),
                employee.getPhone(),
                employee.getEmail(),
                employee.getEducation(),
                employee.getDesignation(),
                employee.getDesignationType(),
                employee.getDepartmentId(),
                employee.getCompany(),
                employee.getShiftType(),
                employee.getPhoto(),
                employee.getPresentStatus());
    }

    // fetch employee by sn
    public EmployeeEntity findBySn(int sn) {
        String sql = "SELECT e.*, d.name AS departmentName " +
                "FROM employee e " +
                "LEFT JOIN departments d ON e.department_id = d.dept_id " +
                "WHERE e.sn = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            EmployeeEntity emp = new EmployeeEntity();
            emp.setSn(rs.getInt("sn"));
            emp.setEmployeeCode(rs.getString("employee_code"));
            emp.setName(rs.getString("name"));
            emp.setGender(rs.getString("gender"));
            emp.setDob(rs.getDate("dob"));
            emp.setJoinDate(rs.getDate("join_date"));
            emp.setResignationDate(rs.getDate("resignation_date"));
            emp.setSalary(rs.getDouble("salary"));
            emp.setAddress(rs.getString("address"));
            emp.setPhone(rs.getString("phone"));
            emp.setEmail(rs.getString("email"));
            emp.setEducation(rs.getString("education"));
            emp.setDesignation(rs.getString("designation"));
            emp.setDesignationType(rs.getString("designation_type"));
            emp.setDepartmentId(rs.getInt("department_id"));
            emp.setDepartmentName(rs.getString("departmentName")); // important
            emp.setCompany(rs.getString("company"));
            emp.setShiftType(rs.getString("shift_type"));
            emp.setPhoto(rs.getString("photo"));
            emp.setPresentStatus(rs.getString("present_status"));
            return emp;
        }, sn);
    }

    // update employee
    public int update(EmployeeEntity emp) {
        String sql = "UPDATE employee SET name = ?, present_status = ?, shift_type = ?, resignation_date = ?, department_id = ? WHERE sn = ?";
        return jdbcTemplate.update(sql,
                emp.getName(),
                emp.getPresentStatus(),
                emp.getShiftType(),
                emp.getResignationDate(),
                emp.getDepartmentId(),
                emp.getSn());
    }

    // delete employee
    public int delete(int sn) {
        String sql = "DELETE FROM employee WHERE sn = ?";
        return jdbcTemplate.update(sql, sn);
    }

    public List<EmployeeEntity> findEmployeesByDepartment(int departmentId) {
        String sql = "SELECT e.sn, e.name, e.salary, d.name AS department_name FROM employee e JOIN departments d ON e.department_id = d.dept_id WHERE e.department_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            EmployeeEntity e = new EmployeeEntity();
            e.setSn(rs.getInt("sn"));
            e.setName(rs.getString("name"));
            e.setSalary(rs.getDouble("salary"));
            e.setDepartmentName(rs.getString("department_name"));
            return e;
        }, departmentId);
    }

    public Double findEmployeeBaseSalary(int employeeSn) {
        String sql = "SELECT salary FROM employee WHERE sn = ?";
        return jdbcTemplate.queryForObject(sql, Double.class, employeeSn);
    }

    // Count total employees
    public int countAll() {
        String sql = "SELECT COUNT(*) FROM employee";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    // Count total departments
    public int countAllDepartments() {
        String sql = "SELECT COUNT(*) FROM departments";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    public int countPresent() {
        String sql = "SELECT COUNT(*) FROM employee WHERE present_status= 'PRESENT'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    public int countAbsent() {
        String sql = "SELECT COUNT(*) FROM employee WHERE present_status = 'ABSENT'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    public int countByGender(String gender) {
        String sql = "SELECT COUNT(*) FROM employee WHERE gender = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, gender);
        return count != null ? count : 0;
    }

    public int countActive() {
        String sql = "SELECT COUNT(*) FROM employee WHERE resignation_date IS NULL";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    public int countResigned() {
        String sql = "SELECT COUNT(*) FROM employee WHERE resignation_date  IS NOT NULL";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    // for chart graph chart
    // count employees by shift
    public Map<String, Integer> countByShift() {
        String sql = "SELECT shift_type,COUNT(sn) AS cnt FROM employee GROUP BY shift_type";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            result.put(
                    String.valueOf(row.get("shift_type")),
                    ((Number) row.get("cnt")).intValue());
        }
        return result;
    }

    // count employee by gender
    public Map<String, Integer> countByGenderMap() {
        String sql = "SELECT gender,COUNT(sn) AS cnt FROM employee GROUP BY gender";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            result.put(
                    String.valueOf(row.get("gender")),
                    ((Number) row.get("cnt")).intValue());
        }
        return result;
    }

    // Count employees by present status
    public Map<String, Integer> countByPresentStatus() {
        String sql = "SELECT present_status, COUNT(sn) AS cnt FROM employee GROUP BY present_status";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map<String, Object> row : rows) {
            result.put(
                    String.valueOf(row.get("present_status")),
                    ((Number) row.get("cnt")).intValue());
        }
        return result;
    }

    public Map<String, Integer> countByDepartment() {
        String sql = """
                    SELECT d.name AS dept, COUNT(e.sn) AS cnt
                    FROM employee e
                    LEFT JOIN departments d ON e.department_id = d.dept_id
                    GROUP BY d.name
                """;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        Map<String, Integer> result = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            result.put(
                    (String) row.get("dept"),
                    ((Number) row.get("cnt")).intValue());
        }
        return result;
    }

    // Method to get all departments for dropdown (returns ID and name)
    public List<Map<String, Object>> findAllDepartments() {
        String sql = "SELECT dept_id, name FROM departments ORDER BY name";
        return jdbcTemplate.queryForList(sql);
    }

    // for active log
    public List<Map<String, Object>> findAllLogs() {
        String sql = """
                SELECT a.action AS action, a.description AS description, a.date_time AS date_time, u.username AS username
                FROM activity_logs a
                LEFT JOIN users u ON a.user_id = u.id
                ORDER BY a.date_time DESC
                """;
        return jdbcTemplate.queryForList(sql);
    }

    public void insertActivitylog(int userId, String action, String description) {
        String sql = "INSERT INTO activity_logs(user_id,action,description) VALUES (?,?,?)";
        jdbcTemplate.update(sql, userId, action, description);
    }

    public void unlinkEmployeeFromUsers(int sn) {
        String sql = "UPDATE users SET employee_sn = NULL WHERE employee_sn = ?";
        jdbcTemplate.update(sql, sn);
    }

    public void deleteUserByEmployeeSn(int sn) {
        String sql = "DELETE FROM users WHERE employee_sn = ?";
        jdbcTemplate.update(sql, sn);
    }

}
