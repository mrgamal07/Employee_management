package com.arun.Brocode.Service;

import com.arun.Brocode.Repository.EmployeeRepository;
import com.arun.Brocode.model.EmployeeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<EmployeeEntity> getPaginatedEmployees(int page, int size) {
        int offset = (page - 1) * size;
        return employeeRepository.findPaginatedWithDepartment(offset, size);
    }

    public int getTotalEmployeePages(int pageSize) {
        int totalEmployees = employeeRepository.countAllEmployees();
        return (int) Math.ceil((double) totalEmployees / pageSize);
    }


    public int addEmployee(EmployeeEntity employeeEntity) {
        return employeeRepository.insert(employeeEntity);
    }

    public EmployeeEntity getEmployeeBySn(int sn) {
        return employeeRepository.findBySn(sn);
    }

    public int updateEmployee(EmployeeEntity employeeEntity) {
        return employeeRepository.update(employeeEntity);
    }

    public int deleteEmployee(int sn) {
        return employeeRepository.delete(sn);
    }

    public int countAll() {
        return employeeRepository.countAll();
    }

    public List<Map<String, Object>> getAllDepartments() {
        return employeeRepository.findAllDepartments();
    }

    public int countAllDepartments() {
        return employeeRepository.countAllDepartments();
    }

    public int countPresent() {
        return employeeRepository.countPresent();
    }

    public int countAbsent() {
        return employeeRepository.countAbsent();
    }

    public int countByGender(String gender) {
        return employeeRepository.countByGender(gender);
    }

    public int countActive() {
        return employeeRepository.countActive();
    }

    public int countResigned() {
        return employeeRepository.countResigned();
    }

    public Map<String, Integer> countByDepartment() {
        return employeeRepository.countByDepartment();
    }

    public Map<String, Integer> countByShift() {
        return employeeRepository.countByShift();
    }

    public Map<String, Integer> countByPresentStatus() {
        return employeeRepository.countByPresentStatus();
    }

    public Map<String, Integer> countByGenderMap() {
        return employeeRepository.countByGenderMap();
    }

    public List<Map<String, Object>> getAllLogs() {
        return employeeRepository.findAllLogs();
    }

    @Transactional
    public void addEmployeeWithLog(EmployeeEntity employee, int adminId) {
        employeeRepository.insert(employee);
        employeeRepository.insertActivitylog(
                adminId,
                "ADD",
                "Added new employee: " + employee.getName());
    }

    @Transactional
    public void deleteEmployeeWithLog(int sn, String name, int adminId) {
        // 1. Delete associated user account if exists
        employeeRepository.deleteUserByEmployeeSn(sn);
        // 2. Delete employee
        employeeRepository.delete(sn);
        // 3. Insert activity log
        employeeRepository.insertActivitylog(
                adminId,
                "DELETE",
                "Deleted employee: " + name);
    }

    @Transactional
    public void updateEmployeeWithLog(EmployeeEntity employee, int adminId) {
        employeeRepository.update(employee);
        employeeRepository.insertActivitylog(
                adminId,
                "UPDATE",
                "Updated employee: " + employee.getName());
    }
}
