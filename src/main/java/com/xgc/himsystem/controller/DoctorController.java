package com.xgc.himsystem.controller;

import com.xgc.himsystem.entity.Doctor;
import com.xgc.himsystem.entity.DoctorInfoDTO;
import com.xgc.himsystem.entity.Schedule;
import com.xgc.himsystem.entity.ScheduleDTO;
import com.xgc.himsystem.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/availableSchedules")
    public List<Schedule> getAvailableDoctors(@RequestParam String date, @RequestParam String department) {
        return doctorService.findAvailableDoctors(date, department);
    }

    @PostMapping("/login")
    public String loginDoctor(@RequestParam String username, @RequestParam String password) {
        Doctor doctor = doctorService.loginDoctor(username, password);
        if (doctor != null) {
            return "Login successful. User type: Doctor";
        } else {
            return "Login failed.";
        }
    }

    @PostMapping("/register")
    public String registerDoctor(@RequestParam String name, @RequestParam String gender,
                                 @RequestParam int age, @RequestParam String contact,
                                 @RequestParam String address, @RequestParam String idNumber,
                                 @RequestParam String password, @RequestParam String confirmPassword,
                                 @RequestParam String specialty, @RequestParam String hospital,
                                 @RequestParam String department, @RequestParam String title) {
        if (!password.equals(confirmPassword)) {
            return "Password and confirm password do not match.";
        }
        boolean success = doctorService.registerDoctor(name, gender, age, contact, address, idNumber,
                password, specialty, hospital, department, title);
        if (success) {
            return "Registration successful.";
        } else {
            return "Registration failed. Contact number already used.";
        }
    }

    @PostMapping("/publish-schedule")
    public String publishSchedule(@RequestParam String doctorUsername, @RequestParam String date,
                                  @RequestParam int availableRegisterNum, @RequestParam boolean isRegular,
                                  @RequestParam(required = false) String frequency) {
        return doctorService.publishSchedule(doctorUsername, date, availableRegisterNum, isRegular, frequency);
    }


    @GetMapping("/schedules")
    public List<ScheduleDTO> getDoctorSchedules(@RequestParam String doctorUsername) {
        return doctorService.getDoctorSchedules(doctorUsername);
    }

    @GetMapping("/schedules-by-date")
    public List<ScheduleDTO> getDoctorSchedulesByDateRange(@RequestParam String doctorUsername,
                                                           @RequestParam String startDate,
                                                           @RequestParam String endDate) {
        return doctorService.getDoctorSchedulesByDateRange(doctorUsername, startDate, endDate);
    }

    @GetMapping("/info")
    public DoctorInfoDTO getDoctorInfo(@RequestParam String doctorUsername) {
        return doctorService.getDoctorInfo(doctorUsername);
    }

    @PostMapping("/change-password")
    public String changeDoctorPassword(@RequestParam String doctorUsername,
                                       @RequestParam String oldPassword,
                                       @RequestParam String newPassword) {
        boolean success = doctorService.changeDoctorPassword(doctorUsername, oldPassword, newPassword);
        if (success) {
            return "Password changed successfully.";
        } else {
            return "Failed to change password. The old password is incorrect.";
        }
    }

    @PostMapping("/update-doctor-info")
    public String updateDoctorInfo(@RequestParam String doctorUsername,
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
        boolean success = doctorService.updateDoctorInfo(doctorUsername, newName, newSpecialty, newHospital, newDepartment, newTitle,
                newGender, newAge, newContactNumber, newAddress, newIdCard);
        if (success) {
            return "Doctor information updated successfully.";
        } else {
            return "Failed to update doctor information. The new contact number is already registered.";
        }
    }
}