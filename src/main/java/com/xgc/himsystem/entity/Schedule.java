package com.xgc.himsystem.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private LocalDate date;
    private int availableRegisterNum;

    private String scheduleNumber;

    private boolean isPublished;

    // 无参构造函数
    public Schedule() {}

    // 全参构造函数
    public Schedule(String scheduleNumber, LocalDate date, int availableRegisterNum, Doctor doctor) {
        this.scheduleNumber = scheduleNumber;
        this.date = date;
        this.availableRegisterNum = availableRegisterNum;
        this.doctor = doctor;
        this.isPublished = false; // 初始化为未发布
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAvailableRegisterNum() {
        return availableRegisterNum;
    }

    public void setAvailableRegisterNum(int availableRegisterNum) {
        this.availableRegisterNum = availableRegisterNum;
    }

    public String getScheduleNumber() {
        return scheduleNumber;
    }

    public void setScheduleNumber(String scheduleNumber) {
        this.scheduleNumber = scheduleNumber;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }
}