// Logout function for sidebar button
function logout() {
    fetch("/logout", { method: "POST" }) // optional, if backend invalidates session
        .finally(() => {
            window.location.href = "/login"; // redirect to login page
        });
}
