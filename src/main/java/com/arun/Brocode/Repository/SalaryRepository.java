package com.arun.Brocode.Repository;

import com.arun.Brocode.model.SalaryPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;

@Repository
public class SalaryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertSalaryPayment(SalaryPayment salaryPayment) {
        String sql = "INSERT INTO salary_payments (employee_sn, salary_year, salary_month, base_salary, bonus, deduction, net_salary, payment_date, paid_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                salaryPayment.getEmployeeSn(),
                salaryPayment.getSalaryYear(),
                salaryPayment.getSalaryMonth(),
                salaryPayment.getBaseSalary(),
                salaryPayment.getBonus(),
                salaryPayment.getDeduction(),
                salaryPayment.getNetSalary(),
                salaryPayment.getPaymentDate(),
                salaryPayment.getPaidBy());
    }

    public List<SalaryPayment> findAllByEmployeeSn(int employeeSn) {
        String sql = "SELECT sp.*, e.name as employee_name, d.name as department_name FROM salary_payments sp JOIN employee e ON sp.employee_sn = e.sn JOIN departments d ON e.department_id = d.dept_id WHERE sp.employee_sn = ? ORDER BY sp.salary_year DESC, FIELD(sp.salary_month, 'JAN','FEB','MAR','APR','MAY','JUN','JUL','AUG','SEP','OCT','NOV','DEC') DESC";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            SalaryPayment sp = new SalaryPayment();
            sp.setSalaryId(rs.getInt("salary_id"));
            sp.setEmployeeSn(rs.getInt("employee_sn"));
            sp.setSalaryYear(rs.getInt("salary_year"));
            sp.setSalaryMonth(rs.getString("salary_month"));
            sp.setBaseSalary(rs.getBigDecimal("base_salary"));
            sp.setBonus(rs.getBigDecimal("bonus"));
            sp.setDeduction(rs.getBigDecimal("deduction"));
            sp.setNetSalary(rs.getBigDecimal("net_salary"));
            sp.setPaymentDate(rs.getDate("payment_date"));
            sp.setPaidBy(rs.getObject("paid_by", Integer.class));
            sp.setCreatedAt(rs.getTimestamp("created_at"));
            sp.setEmployeeName(rs.getString("employee_name"));
            sp.setDepartmentName(rs.getString("department_name"));
            return sp;
        }, employeeSn);
    }

    public boolean isSalaryAlreadyPaid(int employeeSn, int year, String month) {
        String sql = "SELECT COUNT(*) FROM salary_payments WHERE employee_sn = ? AND salary_year = ? AND salary_month = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, employeeSn, year, month);
        return count != null && count > 0;
    }

    // Method to get all departments for dropdown
    public List<Map<String, Object>> findAllDepartments() {
        String sql = "SELECT dept_id, name FROM departments ORDER BY name";
        return jdbcTemplate.queryForList(sql);
    }
}
