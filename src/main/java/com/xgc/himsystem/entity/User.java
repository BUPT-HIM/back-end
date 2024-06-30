package com.xgc.himsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity // 表示该类是一个JPA实体，将映射到数据库表
@Inheritance(strategy = InheritanceType.JOINED) // 继承策略为 JOINED，每个类有自己的表，子类表通过外键关联到父类表
public class User {
    @Id // 标识该字段为主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略为 IDENTITY，由数据库自动生成
    private Long id;

    @Column(unique = true, nullable = false) // 字段唯一且不能为空
    private String idNumber;

    @Column(nullable = false) // 字段不能为空
    private String name;

    private int age;

    private String gender;

    private String address;

    private String contact;

    @Column(unique = true, nullable = false) // 确保用户名唯一且不能为空
    private String username;

    @Column(nullable = false) // 确保密码不能为空
    private String password;

    @Column(nullable = false) // 确保用户类型不能为空
    private String userType;

    private String status;  // 账号状态：有效/无效

    private LocalDateTime registrationTime;

    // 无参构造函数
    public User() {}

    public User(String idNumber, String name, int age, String gender, String address, String contact, String username, String password, String userType, String status, LocalDateTime registrationTime) {
        this.idNumber = idNumber;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.contact = contact;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.status = status;           // 账号状态：有效/无效
        this.registrationTime = registrationTime;
    }

// Getters and Setters


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(LocalDateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
