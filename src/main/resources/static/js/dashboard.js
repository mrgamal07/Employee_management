document.addEventListener("DOMContentLoaded", function () {

    // ===================== DASHBOARD CARDS =====================
    fetch("/api/dashboard-data")
        .then(response => response.json())
        .then(data => {
            // Update dashboard cards
            document.getElementById("totalEmployees").textContent = data.totalEmployees;
            document.getElementById("totalDepartments").textContent = data.totalDepartments;
            document.getElementById("presentToday").textContent = data.presentToday;
            document.getElementById("absentToday").textContent = data.absentToday;
            document.getElementById("maleCount").textContent = data.maleCount;
            document.getElementById("femaleCount").textContent = data.femaleCount;
            document.getElementById("activeCount").textContent = data.activeEmployees;
            document.getElementById("resignedCount").textContent = data.resignedEmployees;
        })
        .catch(error => console.error("Error fetching dashboard data:", error));

    // ===================== CHARTS =====================
    fetch("/api/dashboard-charts")
        .then(response => response.json())
        .then(data => renderCharts(data))
        .catch(error => console.error("Error fetching chart data:", error));

    function renderCharts(data) {
        const chartOptions = {
            responsive: true,
            plugins: {
                legend: {
                    position: "bottom",
                    labels: { font: { size: 12 } }
                }
            },
            layout: { padding: 10 }
        };

        // Attendance Chart
        const attendanceCtx = document.getElementById("attendanceChart").getContext("2d");
        new Chart(attendanceCtx, {
            type: "doughnut",
            data: {
                labels: ["Present", "Absent"],
                datasets: [{
                    data: [data.presentStatusCounts?.PRESENT || 0, data.presentStatusCounts?.ABSENT || 0],
                    backgroundColor: ["#4CAF50", "#F44336"]
                }]
            },
            options: chartOptions
        });

        // Gender Chart
        const genderCtx = document.getElementById("genderChart").getContext("2d");
        new Chart(genderCtx, {
            type: "pie",
            data: {
                labels: Object.keys(data.genderCounts || {}),
                datasets: [{
                    data: Object.values(data.genderCounts || {}),
                    backgroundColor: ["#2196F3", "#FF9800", "#9C27B0"]
                }]
            },
            options: chartOptions
        });

        // Department Chart
        const deptCtx = document.getElementById("departmentChart").getContext("2d");
        new Chart(deptCtx, {
            type: "bar",
            data: {
                labels: Object.keys(data.departmentCounts || {}),
                datasets: [{
                    label: "Employees per Department",
                    data: Object.values(data.departmentCounts || {}),
                    backgroundColor: "#3F51B5"
                }]
            },
            options: chartOptions
        });

        // Shift Chart
        const shiftCtx = document.getElementById("shiftChart").getContext("2d");
        new Chart(shiftCtx, {
            type: "bar",
            data: {
                labels: Object.keys(data.shiftCounts || {}),
                datasets: [{
                    label: "Employees per Shift",
                    data: Object.values(data.shiftCounts || {}),
                    backgroundColor: "#FF5722"
                }]
            },
            options: chartOptions
        });
    }
});

function editEmployee(sn) {
    window.location.href = "/employees/edit/" + sn;
}

function viewEmployee(sn) {
    window.location.href = "/employees/view/" + sn;
}

function deleteEmployee(sn) {
    if (confirm("Are you sure you want to delete this employee?")) {
        fetch("/employees/delete/" + sn, {
            method: "GET"
        })
            .then(response => {
                if (response.ok) {
                    alert("Employee deleted successfully");
                    location.reload();
                } else {
                    alert("Error deleting employee: " + response.statusText);
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("An error occurred while deleting the employee.");
            });
    }
}

function goToAddEmployee() {
    window.location.href = "/employees/add";
}

function goToManageEmployees() {
    window.location.href = "/admin/employees";
}

function goToAddDepartment() {
    window.location.href = "/admin/departments/add";
}

function goToManageDepartments() {
    window.location.href = "/admin/departments";
}

function editDepartment(id) {
    window.location.href = "/admin/departments/edit/" + id;
}

function deleteDepartment(id) {
    if (confirm("Are you sure you want to delete this department?")) {
        window.location.href = "/admin/departments/delete/" + id;
    }
}


function goToDashboard() {
    window.location.href = "/adminDashboard";
}

function logout() {
    if (confirm("Are you sure you want to logout?")) {
        fetch("/auth/logout", {
            method: "POST"
        })
            .then(response => {
                if (response.ok) {
                    window.location.href = "/login";
                } else {
                    alert("Logout failed");
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("An error occurred during logout.");
            });
    }
}

