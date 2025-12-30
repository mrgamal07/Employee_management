package com.arun.Brocode.Controller;

import com.arun.Brocode.Service.DepartmentService;
import com.arun.Brocode.model.DepartmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    // MANAGE departments page
    @GetMapping
    public String manageDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "manage-departments";
    }

    // SHOW add department form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new DepartmentEntity());
        return "add-department";
    }

    // SAVE department
    @PostMapping("/add")
    public String addDepartment(@ModelAttribute DepartmentEntity department) {
        departmentService.addDepartment(department);
        return "redirect:/admin/departments";
    }

    // SHOW edit form
    @GetMapping("/edit/{id}")
    public String editDepartment(@PathVariable int id, Model model) {
        model.addAttribute("department", departmentService.getById(id));
        return "edit-department";
    }

    // UPDATE department
    @PostMapping("/update")
    public String updateDepartment(@ModelAttribute DepartmentEntity department) {
        departmentService.updateDepartment(department);
        return "redirect:/admin/departments";
    }

    // DELETE department
    @GetMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable int id) {
        departmentService.deleteDepartment(id);
        return "redirect:/admin/departments";
    }
}
