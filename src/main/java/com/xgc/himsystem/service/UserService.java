package com.xgc.himsystem.service;

import com.xgc.himsystem.entity.User;
import com.xgc.himsystem.entity.UserDTO;
import com.xgc.himsystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            logger.info("User found: " + user.getUsername());
            logger.info("Database password: " + user.getPassword());
            logger.info("Provided old password: " + oldPassword);
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                userRepository.save(user);
                return true;
            } else {
                logger.info("Password mismatch");
            }
        } else {
            logger.info("User not found");
        }
        return false;
    }

    public boolean updateUserInfo(String username, String newName, String newGender, Integer newAge, String newContact, String newAddress, String newIdNumber) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            if (newName != null && !newName.isEmpty()) {
                user.setName(newName);
            }
            if (newGender != null && !newGender.isEmpty()) {
                user.setGender(newGender);
            }
            if (newAge != null) {
                user.setAge(newAge);
            }
            if (newContact != null && !newContact.isEmpty()) {
                if (userRepository.findByContact(newContact) == null) {
                    user.setContact(newContact);
                } else {
                    return false; // 新的联系方式已经注册
                }
            }
            if (newAddress != null && !newAddress.isEmpty()) {
                user.setAddress(newAddress);
            }
            if (newIdNumber != null && !newIdNumber.isEmpty()) {
                user.setIdNumber(newIdNumber);
            }
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public User login(String username, String password) {
        if (!isValidUsername(username)) {
            return null;
        }
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password) && "有效".equals(user.getStatus())) {
            return user;
        }
        return null;
    }

    public boolean register(String userType, String name, String gender, int age, String contact,
                            String address, String idNumber, String password) {
        // 检查电话号码是否唯一
        if (userRepository.findByContact(contact) != null) {
            return false;
        }

        // 生成用户名
        String username = generateUsername(userType);

        // 创建新用户
        User newUser = new User(idNumber, name, age, gender, address, contact, username,
                                password, userType, "无效", LocalDateTime.now());

        // 保存用户
        userRepository.save(newUser);
        return true;
    }

    private String generateUsername(String userType) {
        int prefix = switch (userType) {
            case "患者" -> (int) (100 + Math.random() * 400);
            case "医生" -> (int) (600 + Math.random() * 200);
            case "管理员" -> 900;
            default -> throw new IllegalArgumentException("无效的用户类型");
        };
        return String.valueOf(prefix) + String.valueOf((int) (1000000 + Math.random() * 9000000));
    }

    private boolean isValidUsername(String username) {
        if (username.length() != 10) {
            return false;
        }
        try {
            int prefix = Integer.parseInt(username.substring(0, 3));
            if ((prefix >= 100 && prefix < 500) || (prefix >= 600 && prefix < 800) || prefix == 900) {
                return true;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }



}
