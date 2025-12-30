package com.arun.Brocode.Controller;

import com.arun.Brocode.Service.EmployeeService;
import com.arun.Brocode.model.EmployeeEntity;
import com.arun.Brocode.model.UserEntity;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    // show add employee form
    @GetMapping("/employees/add")
    public String showAddForm(Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null)
            return "redirect:/login";
        model.addAttribute("employee", new EmployeeEntity());
        model.addAttribute("departments", employeeService.getAllDepartments()); // Add departments
        return "add-employee";
    }

    // handel add employee form submit
    @PostMapping("/employees/add")
    public String addEmployee(@ModelAttribute EmployeeEntity employee,
            @RequestParam(value = "image", required = false) MultipartFile photo,
            Model model, HttpSession session) {
        try {
            if (photo != null && !photo.isEmpty()) {
                // Use absolute path in project directory
                String projectDir = System.getProperty("user.dir");
                String uploadDir = projectDir + "/uploads/";
                String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.createDirectories(filePath.getParent());
                photo.transferTo(filePath);
                // Save file name in employee object
                employee.setPhoto(fileName);
            }

            UserEntity admin = (UserEntity) session.getAttribute("loggedUser");
            if (admin == null) {
                return "redirect:/login";
            }
            int adminId = admin.getId();
            employeeService.addEmployeeWithLog(employee, adminId);
            model.addAttribute("employee", new EmployeeEntity());
            model.addAttribute("message", "Employee added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("employee", employee);
            model.addAttribute("message", "Error: " + e.getMessage());
        }
        return "add-employee";
    }

    @GetMapping("/admin/employees")
    public String adminDashboard(
            Model model,
            HttpSession session,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (session.getAttribute("loggedUser") == null)
            return "redirect:/login";

        List<EmployeeEntity> employeeEntities = employeeService.getPaginatedEmployees(page, size);
        int totalPages = employeeService.getTotalEmployeePages(size);

        model.addAttribute("employees", employeeEntities);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", size);
        return "admin";
    }

    @PostMapping("/employees/update")
    public String updateEmployee(@ModelAttribute EmployeeEntity employee, Model model, HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("loggedUser");
        if (admin == null) {
            return "redirect:/login";
        }
        int adminId = admin.getId();
        employeeService.updateEmployeeWithLog(employee, adminId);
        return "redirect:/admin/employees";
    }

    @GetMapping("/employees/edit/{sn}")
    public String showEditForm(@PathVariable("sn") int sn, Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null)
            return "redirect:/login";
        EmployeeEntity employee = employeeService.getEmployeeBySn(sn);
        model.addAttribute("employee", employee);
        return "update-employee"; // the HTML form template for editing
    }

    @GetMapping("/employees/view/{sn}")
    public String viewEmployee(@PathVariable("sn") int sn, Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null)
            return "redirect:/login";
        EmployeeEntity employee = employeeService.getEmployeeBySn(sn);
        model.addAttribute("employee", employee);
        return "view-employee"; // create view-employee.html to show details
    }

    @GetMapping("/employees/delete/{sn}")
    @ResponseBody
    public org.springframework.http.ResponseEntity<String> deleteEmployee(@PathVariable int sn, HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("loggedUser");
        if (admin == null) {
            return org.springframework.http.ResponseEntity.status(401).body("Unauthorized");
        }
        int adminId = admin.getId();

        EmployeeEntity employee = employeeService.getEmployeeBySn(sn);
        if (employee != null) {
            employeeService.deleteEmployeeWithLog(sn, employee.getName(), adminId);
            return org.springframework.http.ResponseEntity.ok("Deleted successfully");
        }
        return org.springframework.http.ResponseEntity.status(404).body("Employee not found");
    }

    @GetMapping("/adminDashboard")
    public String showAdminDashboard(Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null)
            return "redirect:/login";
        return "adminDashboard";
    }

    @GetMapping("/admin/activity-logs")
    public String showLogs(Model model, HttpSession session) {
        if (session.getAttribute("loggedUser") == null)
            return "redirect:/login";
        model.addAttribute("logs", employeeService.getAllLogs());
        return "activity-logs";
    }

    // employee dashboard controller
    @GetMapping("/employee/dashboard")
    public String employeeDashboard(HttpSession session, Model model) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getEmployeeSn() == null) {
            return "redirect:/login";
        }
        model.addAttribute(
                "employee",
                employeeService.getEmployeeBySn(loggedUser.getEmployeeSn()));
        return "employee-dashboard";
    }

}
