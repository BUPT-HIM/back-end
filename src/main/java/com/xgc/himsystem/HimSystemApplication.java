package com.xgc.himsystem;

import com.xgc.himsystem.entity.User;
import com.xgc.himsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class HimSystemApplication implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(HimSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 初始化一些用户数据
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByUsername("1001234567"));
        try {
            if (existingUser.isEmpty()) {
                User user = new User();
                user.setIdNumber("123456789");
                user.setName("John Doe");
                user.setAge(30);
                user.setGender("Male");
                user.setAddress("123 Main St");
                user.setContact("123-456-7890");
                user.setUsername("1001234567");
                user.setPassword("password");
                user.setUserType("admin");

                userRepository.save(user);
                System.out.println("User saved successfully");
            } else {
                System.out.println("User already exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
