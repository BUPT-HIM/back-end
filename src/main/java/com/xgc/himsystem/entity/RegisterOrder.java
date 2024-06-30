package com.xgc.himsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class RegisterOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String orderNumber;
    private LocalDate date;
    private String department;
    private String doctorName;
    private LocalDateTime createdTime;

    private String appointmentStatus;

    private String visitStatus;

    // 无参构造函数
    public RegisterOrder() {}

    // 全参构造函数
    public RegisterOrder(String orderNumber, LocalDate date, String department, String doctorName, LocalDateTime createdTime, String appointmentStatus, String visitStatus) {
        this.orderNumber = orderNumber;
        this.date = date;
        this.department = department;
        this.doctorName = doctorName;
        this.createdTime = createdTime;
        this.appointmentStatus = appointmentStatus;
        this.visitStatus = visitStatus;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getVisitStatus() {
        return visitStatus;
    }

    public void setVisitStatus(String visitStatus) {
        this.visitStatus = visitStatus;
    }
}
