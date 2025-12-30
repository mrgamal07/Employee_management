package com.arun.Brocode.Service;

import com.arun.Brocode.Repository.UserRepository;
import com.arun.Brocode.model.Role;
import com.arun.Brocode.model.Status;
import com.arun.Brocode.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // signup
    public String signup(UserEntity userEntity) {
        userEntity.setRole(Role.EMPLOYEE);
        userEntity.setStatus(Status.ACTIVE);
        int result = userRepository.saveUserEntity(userEntity);
        return result > 0 ? "signup successful" : "signup failed";
    }

    // Login
    public UserEntity login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    // Get employee list for dropdown
    public List<Map<String, Object>> getEmployees() {
        return userRepository.findAllEmployees();
    }

}
