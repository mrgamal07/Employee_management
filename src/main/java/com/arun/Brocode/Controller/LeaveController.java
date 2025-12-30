package com.arun.Brocode.Controller;

import com.arun.Brocode.Service.LeaveService;
import com.arun.Brocode.Service.EmployeeService; // Assuming you need employee service to get employee data for display
import com.arun.Brocode.model.LeaveRequest;
import com.arun.Brocode.model.UserEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import com.arun.Brocode.model.Role;
import java.util.Date;
import java.util.List;

@Controller
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private EmployeeService employeeService; // For fetching employee name for admin view

    // === Employee Endpoints ===

    @GetMapping("/employee/leave/apply")
    public String showApplyLeaveForm(Model model, HttpSession session) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getEmployeeSn() == null) {
            return "redirect:/login";
        }
        model.addAttribute("leaveRequest", new LeaveRequest());
        return "employee-leave-apply";
    }

    @PostMapping("/employee/leave/apply")
    public String applyLeave(@ModelAttribute LeaveRequest leaveRequest, HttpSession session, Model model) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getEmployeeSn() == null) {
            return "redirect:/login";
        }
        leaveRequest.setEmployeeSn(loggedUser.getEmployeeSn());
        int result = leaveService.applyForLeave(leaveRequest);

        if (result > 0) {
            model.addAttribute("message", "Leave application submitted successfully!");
            model.addAttribute("messageType", "success");
        } else {
            model.addAttribute("message", "Failed to submit leave application.");
            model.addAttribute("messageType", "danger");
        }
        model.addAttribute("leaveRequest", new LeaveRequest()); // Clear form
        return "employee-leave-apply";
    }

    @GetMapping("/employee/leave/history")
    public String showEmployeeLeaveHistory(Model model, HttpSession session) {
        UserEntity loggedUser = (UserEntity) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getEmployeeSn() == null) {
            return "redirect:/login";
        }
        List<LeaveRequest> leaveHistory = leaveService.getEmployeeLeaveHistory(loggedUser.getEmployeeSn());
        model.addAttribute("leaveHistory", leaveHistory);
        return "employee-leave-history";
    }

    // === Admin Endpoints ===

    @GetMapping("/admin/leave")
    public String manageLeaveRequests(Model model, HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("loggedUser");
        if (admin == null || admin.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }
        List<LeaveRequest> pendingLeaves = leaveService.getAllPendingLeaveRequests();
        model.addAttribute("pendingLeaves", pendingLeaves);
        // To get employee names for display in admin view (optional, if needed)
        model.addAttribute("employeeService", employeeService);
        return "admin-leave-management";
    }

    @PostMapping("/admin/leave/update-status")
    public String updateLeaveStatus(
            @RequestParam("leaveId") int leaveId,
            @RequestParam("status") String status,
            @RequestParam("adminRemarks") String adminRemarks,
            HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("loggedUser");
        if (admin == null || admin.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }
        leaveService.updateLeaveStatus(leaveId, status, admin.getId(), adminRemarks);
        return "redirect:/admin/leave";
    }

    @GetMapping("/admin/leave/delete/{leaveId}")
    public String deleteLeaveRequest(@PathVariable("leaveId") int leaveId, HttpSession session) {
        UserEntity admin = (UserEntity) session.getAttribute("loggedUser");
        if (admin == null || admin.getRole() != Role.ADMIN) {
            return "redirect:/login";
        }
        leaveService.deleteLeaveRequest(leaveId);
        return "redirect:/admin/leave";
    }

}
