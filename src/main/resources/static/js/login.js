document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("loginForm");
    const loginError = document.getElementById("loginError");

    loginForm.addEventListener("submit", (e) => {
        e.preventDefault();

        const data = {
            username: document.getElementById("username").value,
            password: document.getElementById("password").value
        };

        fetch("/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        })
        .then(res => res.text())
        .then(role => {
            if (role === "invalid") {
                loginError.textContent = "Invalid username or password!";
                return;
            }

            // Redirect based on role
            if (role === "ADMIN") {
                window.location.href = "/adminDashboard";
            } else if (role === "EMPLOYEE") {
                window.location.href = "/employee/dashboard"; // session will handle which employee
            }
        })
        .catch(err => {
            loginError.textContent = "Error connecting to server.";
            console.error(err);
        });
    });
});
