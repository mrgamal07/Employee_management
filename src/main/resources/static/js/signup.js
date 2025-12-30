document.addEventListener("DOMContentLoaded", () => {
    const signupForm = document.getElementById("signupForm");
    const signupError = document.getElementById("signupError");
    const employeeSelect = document.getElementById("employeeSelect");


    // Fetch employee list for dropdown
    fetch("/employees")
        .then(res => res.json())
        .then(data => {
            const select = document.getElementById("employeeSelect");
            data.forEach(emp => {
                if(emp.sn && emp.name){ // make sure both exist
                    const option = document.createElement("option");
                    option.value = emp.sn; // this will be saved as employee_sn
                    option.text = `${emp.sn} - ${emp.name}`; // display sn and name
                    select.appendChild(option);
                }
            });
        })
        .catch(err => console.error("Error fetching employees:", err));



    // Handle signup form submit
    signupForm.addEventListener("submit", (e) => {
        e.preventDefault();

        const userData = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value,
            employeeSn:employeeSelect.value
        };

        if (!userData.employeeSn) {
            signupError.textContent = "Please select your employee!";
            return;
        }

        fetch("/signup", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userData)
        })
        .then(res => res.text())
        .then(msg => {
            if (msg.includes("successful")) {
                alert("Signup successful! Please login.");
                window.location.href = "/login";
            } else {
                signupError.textContent = "Signup failed. Try again.";
            }
        })
        .catch(err => {
            signupError.textContent = "Error connecting to server.";
            console.error(err);
        });
    });
});
