package com.xgc.himsystem.controller;

import com.xgc.himsystem.entity.*;
import com.xgc.himsystem.service.AdminService;
import com.xgc.himsystem.service.ScheduleService;
import com.xgc.himsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/login")
    public String loginAdmin(@RequestParam String username, @RequestParam String password) {
        User admin = userService.login(username, password);
        if (admin != null && "管理员".equals(admin.getUserType())) {
            return "Login successful. User type: Admin";
        } else {
            return "Login failed.";
        }
    }

    @PostMapping("/register")
    public String registerAdmin(@RequestParam String userType, @RequestParam String name, @RequestParam String gender,
                                @RequestParam int age, @RequestParam String contact, @RequestParam String address,
                                @RequestParam String idNumber, @RequestParam String password, @RequestParam String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return "Password and confirm password do not match.";
        }
        boolean success = userService.register(userType, name, gender, age, contact, address, idNumber, password);
        if (success) {
            return "Registration successful.";
        } else {
            return "Registration failed. Contact number already used.";
        }
    }

    @GetMapping("/schedules")
    public List<ScheduleDTO> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    @PostMapping("/review-schedule")
    public String reviewSchedule(@RequestParam String scheduleNumber, @RequestParam boolean auditResult) {
        boolean success = scheduleService.reviewSchedule(scheduleNumber, auditResult);
        if (success) {
            return auditResult ? "Schedule published successfully." : "Schedule deleted successfully.";
        } else {
            return "Schedule not found.";
        }
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        return adminService.getAllUsers();
    }

    /**
     * 修改用户状态
     *
     * @param username 用户账号
     * @param newStatus 修改后的注册状态
     * @return 修改结果
     */
    @PostMapping("/update-user-status")
    public String updateUserStatus(@RequestParam String username, @RequestParam boolean newStatus) {
        boolean success = adminService.updateUserStatus(username, newStatus);
        if (success) {
            return newStatus ? "User status updated to active." : "User status updated to inactive.";
        } else {
            return "User not found.";
        }
    }

    /**
     * 根据用户账号查询详细信息
     *
     * @param username 用户账号
     * @return 用户详细信息
     */
    @GetMapping("/user-detail")
    public UserDetailDTO getUserDetail(@RequestParam String username) {
        return adminService.getUserDetail(username);
    }

    /**
     * 获取所有医生的概览信息
     *
     * @return 医生概览信息列表
     */
    @GetMapping("/doctors")
    public List<DoctorOverviewDTO> getAllDoctors() {
        return adminService.getAllDoctors();
    }

    /**
     * 根据用户账号查询医生详细信息
     *
     * @param username 用户账号
     * @return 医生详细信息
     */
    @GetMapping("/doctor-detail")
    public DoctorDetailDTO getDoctorDetail(@RequestParam String username) {
        return adminService.getDoctorDetail(username);
    }

    /**
     * 修改医生信息
     *
     * @param username 用户账号
     * @param newName 新的姓名
     * @param newSpecialty 新的所属专业
     * @param newHospital 新的所属医院
     * @param newDepartment 新的所属科室
     * @param newTitle 新的所属职称
     * @param newGender 新的性别
     * @param newAge 新的年龄
     * @param newContactNumber 新的联系方式
     * @param newAddress 新的地址
     * @param newIdCard 新的身份证号
     * @return 修改结果
     */
    @PostMapping("/update-doctor-info")
    public String updateDoctorInfo(@RequestParam String username,
                                   @RequestParam(required = false) String newName,
                                   @RequestParam(required = false) String newSpecialty,
                                   @RequestParam(required = false) String newHospital,
                                   @RequestParam(required = false) String newDepartment,
                                   @RequestParam(required = false) String newTitle,
                                   @RequestParam(required = false) String newGender,
                                   @RequestParam(required = false) Integer newAge,
                                   @RequestParam(required = false) String newContactNumber,
                                   @RequestParam(required = false) String newAddress,
                                   @RequestParam(required = false) String newIdCard) {
        boolean success = adminService.updateDoctorInfo(username, newName, newSpecialty, newHospital, newDepartment, newTitle,
                newGender, newAge, newContactNumber, newAddress, newIdCard);
        if (success) {
            return "Doctor information updated successfully.";
        } else {
            return "Failed to update doctor information. The new contact number is already registered.";
        }
    }

    /**
     * 获取所有患者的概览信息
     *
     * @return 患者概览信息列表
     */
    @GetMapping("/patients")
    public List<PatientOverviewDTO> getAllPatients() {
        return adminService.getAllPatients();
    }

    /**
     * 根据用户账号查询患者详细信息
     *
     * @param username 用户账号
     * @return 患者详细信息
     */
    @GetMapping("/patient-detail")
    public PatientDetailDTO getPatientDetail(@RequestParam String username) {
        return adminService.getPatientDetail(username);
    }

    /**
     * 修改患者信息
     *
     * @param username 用户账号
     * @param newName 新的姓名
     * @param newGender 新的性别
     * @param newAge 新的年龄
     * @param newContact 新的联系方式
     * @param newAddress 新的地址
     * @param newIdNumber 新的身份证号
     * @return 修改结果
     */
    @PostMapping("/update-patient-info")
    public String updatePatientInfo(@RequestParam String username,
                                    @RequestParam(required = false) String newName,
                                    @RequestParam(required = false) String newGender,
                                    @RequestParam(required = false) Integer newAge,
                                    @RequestParam(required = false) String newContact,
                                    @RequestParam(required = false) String newAddress,
                                    @RequestParam(required = false) String newIdNumber) {
        boolean success = adminService.updatePatientInfo(username, newName, newGender, newAge, newContact, newAddress, newIdNumber);
        if (success) {
            return "Patient information updated successfully.";
        } else {
            return "Failed to update patient information. The new contact number is already registered.";
        }
    }

    /**
     * 根据管理员账号查询基础信息
     *
     * @param username 管理员账号
     * @return 管理员基础信息
     */
    @GetMapping("/admin-detail")
    public AdminDetailDTO getAdminDetail(@RequestParam String username) {
        return adminService.getAdminDetail(username);
    }

    /**
     * 修改管理员密码
     *
     * @param username 用户账号
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 修改结果
     */
    @PostMapping("/change-admin-password")
    public String changeAdminPassword(@RequestParam String username, @RequestParam String oldPassword, @RequestParam String newPassword) {
        boolean success = adminService.changeAdminPassword(username, oldPassword, newPassword);
        if (success) {
            return "Password changed successfully.";
        } else {
            return "Failed to change password. The old password is incorrect.";
        }
    }

    /**
     * 修改管理员个人信息
     *
     * @param username 用户账号
     * @param newName 新的姓名
     * @param newGender 新的性别
     * @param newAge 新的年龄
     * @param newContact 新的联系方式
     * @param newAddress 新的地址
     * @param newIdNumber 新的身份证号
     * @return 修改结果
     */
    @PostMapping("/update-admin-info")
    public String updateAdminInfo(@RequestParam String username,
                                  @RequestParam(required = false) String newName,
                                  @RequestParam(required = false) String newGender,
                                  @RequestParam(required = false) Integer newAge,
                                  @RequestParam(required = false) String newContact,
                                  @RequestParam(required = false) String newAddress,
                                  @RequestParam(required = false) String newIdNumber) {
        boolean success = adminService.updateAdminInfo(username, newName, newGender, newAge, newContact, newAddress, newIdNumber);
        if (success) {
            return "Admin information updated successfully.";
        } else {
            return "Failed to update admin information. The new contact number is already registered.";
        }
    }




}