package com.arun.Brocode.Controller;

import com.arun.Brocode.Service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DashboardApiController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/api/dashboard-data")
    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalEmployees", employeeService.countAll());
        data.put("totalDepartments", employeeService.countAllDepartments());
        data.put("presentToday", employeeService.countPresent());
        data.put("absentToday", employeeService.countAbsent());
        data.put("maleCount", employeeService.countByGender("MALE"));
        data.put("femaleCount", employeeService.countByGender("FEMALE"));
        data.put("otherCount", employeeService.countByGender("OTHER"));
        data.put("activeEmployees", employeeService.countActive());
        data.put("resignedEmployees", employeeService.countResigned());
        return data;
    }

    @GetMapping("/api/dashboard-charts")
    public Map<String, Object> getDashboardCharts() {
        Map<String, Object> chartsData = new HashMap<>();
        chartsData.put("presentStatusCounts", employeeService.countByPresentStatus());
        chartsData.put("genderCounts", employeeService.countByGenderMap());
        chartsData.put("departmentCounts", employeeService.countByDepartment());
        chartsData.put("shiftCounts", employeeService.countByShift());
        return chartsData;
    }
}
