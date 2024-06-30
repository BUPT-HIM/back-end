package com.xgc.himsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
public class Admin extends User {
    // 无参构造函数
    public Admin() {}

    // 全参构造函数
    public Admin(String username, String password, String userType, String name, String gender, int age,
                 String contact, String address, String idNumber, String status, LocalDateTime registrationTime) {
        super(idNumber, name, age, gender, address, contact, username, password, userType, status, registrationTime);
    }

}