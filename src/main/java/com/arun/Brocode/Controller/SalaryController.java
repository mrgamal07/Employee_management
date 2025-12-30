package com.arun.Brocode.Controller;

import com.arun.Brocode.Service.SalaryService;
import com.arun.Brocode.model.EmployeeEntity;
import com.arun.Brocode.model.SalaryPayment;
import com.arun.Brocode.model.UserEntity;
import com.arun.Brocode.model.Role;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    // === Admin Endpoints ===

    @GetMapping("/admin/salary/pay")
    public String showPaySalaryForm(Model model, HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("loggedUser");
        if (admin == null || admin.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }

        model.addAttribute("salaryPayment", new SalaryPayment());
        model.addAttribute("departments", salaryService.getAllDepartments());
        
        // Prepare years for dropdown (e.g., current year - 5 to current year + 1)
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Integer> years = Arrays.asList(currentYear - 5, currentYear - 4, currentYear - 3, currentYear - 2, currentYear - 1, currentYear, currentYear + 1);
        model.addAttribute("years", years);

        // Prepare months for dropdown
        List<String> months = Arrays.asList("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");
        model.addAttribute("months", months);

        return "admin-salary-pay";
    }

    @PostMapping("/admin/salary/pay")
    public String processPaySalary(@ModelAttribute SalaryPayment salaryPayment, HttpSession session, Model model) {
        UserEntity admin = (UserEntity) session.getAttribute("loggedUser");
        if (admin == null || admin.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }
        salaryPayment.setPaidBy(admin.getId());
        salaryPayment.setPaymentDate(new Date()); // Set payment date to current date

        int result = salaryService.processSalaryPayment(salaryPayment);

        if (result > 0) {
            model.addAttribute("message", "Salary paid successfully!");
            model.addAttribute("messageType", "success");
        } else if (result == -1) {
            model.addAttribute("message", "Salary for this employee and month/year has already been paid.");
            model.addAttribute("messageType", "warning");
        } else {
            model.addAttribute("message", "Failed to process salary payment.");
            model.addAttribute("messageType", "danger");
        }
        
        // Re-add necessary attributes for form display
        model.addAttribute("salaryPayment", new SalaryPayment());
        model.addAttribute("departments", salaryService.getAllDepartments());
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        List<Integer> years = Arrays.asList(currentYear - 5, currentYear - 4, currentYear - 3, currentYear - 2, currentYear - 1, currentYear, currentYear + 1);
        model.addAttribute("years", years);
        List<String> months = Arrays.asList("JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");
        model.addAttribute("months", months);

        return "admin-salary-pay";
    }

    @GetMapping("/admin/salary/employees-by-department/{deptId}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getEmployeesByDepartment(@PathVariable("deptId") int deptId) {
        List<EmployeeEntity> employees = salaryService.getEmployeesByDepartment(deptId);
        List<Map<String, Object>> employeeList = employees.stream()
                .map(emp -> Map.of("sn", (Object) emp.getSn(), "name", (Object) emp.getName(), "salary", (Object) emp.getSalary()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(employeeList);
    }

    @GetMapping("/admin/salary/employee-base-salary/{employeeSn}")
    @ResponseBody
    public ResponseEntity<Double> getEmployeeBaseSalary(@PathVariable("employeeSn") int employeeSn) {
        Double baseSalary = salaryService.getEmployeeBaseSalary(employeeSn);
        return ResponseEntity.ok(baseSalary);
    }

    // === Employee Endpoints ===

    @GetMapping("/employee/salary/history")
    public String showEmployeeSalaryHistory(Model model, HttpSession session) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getEmployeeSn() == null) {
            return "redirect:/login";
        }
        List<SalaryPayment> salaryHistory = salaryService.getEmployeeSalaryHistory(loggedUser.getEmployeeSn());
        model.addAttribute("salaryHistory", salaryHistory);
        return "employee-salary-history";
    }
}
