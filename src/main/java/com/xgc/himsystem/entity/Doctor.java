package com.xgc.himsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Doctor extends User {
    private String department; // 科室
    private String specialty; // 专业
    private String hospital; // 所属医院
    private String title; // 职称

    // 无参构造函数
    public Doctor() {}

    // 全参构造函数
    public Doctor(String username, String password, String userType, String name, String gender, int age,
                  String contact, String address, String idNumber, String status, LocalDateTime registrationTime,
                  String specialty, String hospital, String department, String title) {
        super(idNumber, name, age, gender, address, contact, username, password, userType, status, registrationTime);
        this.specialty = specialty;
        this.hospital = hospital;
        this.department = department;
        this.title = title;
    }

    // Getters and Setters
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

