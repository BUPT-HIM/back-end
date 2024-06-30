package com.xgc.himsystem.controller;

import com.xgc.himsystem.entity.User;
import com.xgc.himsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        User user = userService.login(username, password);
        if (user != null) {
            return "Login successful. User type: " + user.getUserType();
        } else {
            return "Login failed.";
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam String userType, @RequestParam String name, @RequestParam String gender,
                           @RequestParam int age, @RequestParam String contact, @RequestParam String address,
                           @RequestParam String idNumber, @RequestParam String password, @RequestParam String confirmPassword) {
        // 确认密码在前端已经处理，这里直接调用注册服务
        boolean success = userService.register(userType, name, gender, age, contact, address,
                            idNumber, password);
        if (success) {
            return "Registration successful.";
        } else {
            return "Registration failed. Contact number already used.";
        }
    }

    @GetMapping("/user-info")
    public User getUserInfo(@RequestParam String username) {
        return userService.findUserByUsername(username);
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String username, @RequestParam String oldPassword, @RequestParam String newPassword) {
        boolean success = userService.changePassword(username, oldPassword, newPassword);
        if (success) {
            return "Password changed successfully.";
        } else {
            return "Failed to change password. The old password is incorrect.";
        }
    }

    @PostMapping("/update-user-info")
    public String updateUserInfo(@RequestParam String username,
                                 @RequestParam(required = false) String newName,
                                 @RequestParam(required = false) String newGender,
                                 @RequestParam(required = false) Integer newAge,
                                 @RequestParam(required = false) String newContact,
                                 @RequestParam(required = false) String newAddress,
                                 @RequestParam(required = false) String newIdNumber) {
        boolean success = userService.updateUserInfo(username, newName, newGender, newAge, newContact, newAddress, newIdNumber);
        if (success) {
            return "User information updated successfully.";
        } else {
            return "Failed to update user information. The new contact number is already registered.";
        }
    }
}
