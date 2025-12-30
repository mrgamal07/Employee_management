package com.arun.Brocode.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddEmployee_ShouldInsertData() throws Exception {
        // Create a mock image file
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "demo-avatar.png",
                "image/png",
                "dummy image content".getBytes());

        // Perform the POST request to /employees/add
        // We simulate the form fields that were causing issues (dates) + the new file
        // param
        mockMvc.perform(multipart("/employees/add")
                .file(image)
                .param("employeeCode", "DEMO-001")
                .param("name", "Demo User")
                .param("gender", "MALE")
                .param("dob", "1990-01-01") // Testing Date Format fix
                .param("joinDate", "2025-01-01") // Testing Date Format fix
                .param("resignationDate", "2030-01-01") // Testing Date Format fix
                .param("salary", "50000")
                .param("address", "123 Demo Street")
                .param("phone", "1234567890")
                .param("email", "demo@example.com")
                .param("education", "B.Tech")
                .param("designation", "Software Engineer")
                .param("designationType", "FULL_TIME")
                .param("departmentId", "1")
                .param("company", "Brocode Inc")
                .param("shiftType", "MORNING")
                .param("presentStatus", "PRESENT"))
                .andDo(print())
                .andExpect(status().isOk()) // Should return 200 OK (view)
                .andExpect(view().name("add-employee")); // Should check if it returns the correct view
    }
}
