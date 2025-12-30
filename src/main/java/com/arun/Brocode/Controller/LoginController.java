package com.arun.Brocode.Controller;

import com.arun.Brocode.Service.UserService;
import com.arun.Brocode.model.Role;
import com.arun.Brocode.model.UserEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    public String landingPage() {
        return "Index";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    // api (ajax)
    // signup api (ajax)
    @PostMapping("/signup")
    @ResponseBody
    public String signup(@RequestBody UserEntity userEntity) {
        return userService.signup(userEntity);
    }

    // employee dropdown
    @GetMapping("/employees")
    @ResponseBody
    public List<Map<String, Object>> employees() {
        return userService.getEmployees();
    }

    @PostMapping("/auth/login")
    @ResponseBody
    public String login(@RequestBody UserEntity user, HttpSession session) {
        UserEntity loggedUser = userService.login(user.getUsername(), user.getPassword());
        if (loggedUser == null) {
            return "invalid";
        }
        session.setAttribute("loggedUser", loggedUser);
        if (loggedUser.getRole() == Role.ADMIN) {
            return "ADMIN";
        } else {
            return "EMPLOYEE";
        }
    }

    @PostMapping("/auth/logout")
    @ResponseBody
    public String logout(HttpSession session) {
        session.invalidate();
        return "success";
    }

}
