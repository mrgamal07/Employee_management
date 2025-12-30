package com.arun.Brocode.Service;

import com.arun.Brocode.Repository.EmployeeRepository;
import com.arun.Brocode.Repository.SalaryRepository;
import com.arun.Brocode.model.EmployeeEntity;
import com.arun.Brocode.model.SalaryPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public int processSalaryPayment(SalaryPayment salaryPayment) {
        // Basic validation: Check if salary already paid for the month/year
        if (salaryRepository.isSalaryAlreadyPaid(salaryPayment.getEmployeeSn(), salaryPayment.getSalaryYear(), salaryPayment.getSalaryMonth())) {
            // You might want to throw a custom exception or return a specific error code
            return -1; // Indicate duplicate payment
        }

        // Calculate net salary if not already set (e.g., if UI doesn't send it)
        if (salaryPayment.getNetSalary() == null) {
            BigDecimal base = salaryPayment.getBaseSalary() != null ? salaryPayment.getBaseSalary() : BigDecimal.ZERO;
            BigDecimal bonus = salaryPayment.getBonus() != null ? salaryPayment.getBonus() : BigDecimal.ZERO;
            BigDecimal deduction = salaryPayment.getDeduction() != null ? salaryPayment.getDeduction() : BigDecimal.ZERO;
            salaryPayment.setNetSalary(base.add(bonus).subtract(deduction));
        }

        return salaryRepository.insertSalaryPayment(salaryPayment);
    }

    public List<SalaryPayment> getEmployeeSalaryHistory(int employeeSn) {
        return salaryRepository.findAllByEmployeeSn(employeeSn);
    }

    public List<Map<String, Object>> getAllDepartments() {
        return salaryRepository.findAllDepartments();
    }

    public List<EmployeeEntity> getEmployeesByDepartment(int departmentId) {
        return employeeRepository.findEmployeesByDepartment(departmentId);
    }

    public Double getEmployeeBaseSalary(int employeeSn) {
        return employeeRepository.findEmployeeBaseSalary(employeeSn);
    }
}

