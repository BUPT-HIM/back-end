package com.xgc.himsystem.service;

import com.xgc.himsystem.entity.*;
import com.xgc.himsystem.repository.AdminRepository;
import com.xgc.himsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 获取所有用户的信息
     *
     * @return 用户信息列表
     */
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return users.stream().map(user -> {
            UserDTO dto = new UserDTO();
            dto.setUsername(user.getUsername());
            dto.setUserType(user.getUserType());
            dto.setName(user.getName());
            dto.setStatus("有效".equals(user.getStatus()));
            dto.setRegistrationDate(LocalDateTime.parse(user.getRegistrationTime().format(formatter)));
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 修改用户状态
     *
     * @param username 用户账号
     * @param newStatus 修改后的注册状态
     * @return 是否修改成功
     */
    public boolean updateUserStatus(String username, boolean newStatus) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setStatus(newStatus ? "有效" : "无效");
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * 根据用户账号查询详细信息
     *
     * @param username 用户账号
     * @return 用户详细信息DTO
     */
    public UserDetailDTO getUserDetail(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }

        UserDetailDTO dto = new UserDetailDTO();
        dto.setUsername(user.getUsername());
        dto.setUserType(user.getUserType());
        dto.setName(user.getName());
        dto.setGender(user.getGender());
        dto.setAge(user.getAge());
        dto.setContact(user.getContact());
        dto.setAddress(user.getAddress());
        dto.setIdNumber(user.getIdNumber());

        if ("医生".equals(user.getUserType())) {
            Doctor doctor = (Doctor) user;
            dto.setSpecialty(doctor.getSpecialty());
            dto.setHospital(doctor.getHospital());
            dto.setDepartment(doctor.getDepartment());
            dto.setTitle(doctor.getTitle());
        }

        return dto;
    }

    /**
     * 获取所有医生的概览信息
     *
     * @return 医生概览信息列表
     */
    public List<DoctorOverviewDTO> getAllDoctors() {
        List<Doctor> doctors = userRepository.findAll().stream()
                .filter(user -> user instanceof Doctor)
                .map(user -> (Doctor) user)
                .toList();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return doctors.stream().map(doctor -> {
            DoctorOverviewDTO dto = new DoctorOverviewDTO();
            dto.setUsername(doctor.getUsername());
            dto.setName(doctor.getName());
            dto.setContactNumber(doctor.getContact());
            dto.setStatus("有效".equals(doctor.getStatus()));
            dto.setRegistrationDate(doctor.getRegistrationTime().format(formatter));
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 根据用户账号查询医生详细信息
     *
     * @param username 用户账号
     * @return 医生详细信息DTO
     */
    public DoctorDetailDTO getDoctorDetail(String username) {
        User user = userRepository.findByUsername(username);
        if (!(user instanceof Doctor doctor)) {
            return null;
        }

        DoctorDetailDTO dto = new DoctorDetailDTO();
        dto.setUsername(doctor.getUsername());
        dto.setUserType(doctor.getUserType());
        dto.setName(doctor.getName());
        dto.setSpecialty(doctor.getSpecialty());
        dto.setHospital(doctor.getHospital());
        dto.setDepartment(doctor.getDepartment());
        dto.setTitle(doctor.getTitle());
        dto.setGender(doctor.getGender());
        dto.setAge(doctor.getAge());
        dto.setContactNumber(doctor.getContact());
        dto.setAddress(doctor.getAddress());
        dto.setIdCard(doctor.getIdNumber());

        return dto;
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
     * @param newContact 新的联系方式
     * @param newAddress 新的地址
     * @param newIdNumber 新的身份证号
     * @return 是否修改成功
     */
    public boolean updateDoctorInfo(String username, String newName, String newSpecialty, String newHospital,
                                    String newDepartment, String newTitle, String newGender, Integer newAge,
                                    String newContact, String newAddress, String newIdNumber) {
        User user = userRepository.findByUsername(username);
        if (!(user instanceof Doctor doctor)) {
            return false;
        }

        if (newName != null && !newName.isEmpty()) {
            doctor.setName(newName);
        }
        if (newSpecialty != null && !newSpecialty.isEmpty()) {
            doctor.setSpecialty(newSpecialty);
        }
        if (newHospital != null && !newHospital.isEmpty()) {
            doctor.setHospital(newHospital);
        }
        if (newDepartment != null && !newDepartment.isEmpty()) {
            doctor.setDepartment(newDepartment);
        }
        if (newTitle != null && !newTitle.isEmpty()) {
            doctor.setTitle(newTitle);
        }
        if (newGender != null && !newGender.isEmpty()) {
            doctor.setGender(newGender);
        }
        if (newAge != null) {
            doctor.setAge(newAge);
        }
        if (newContact != null && !newContact.isEmpty()) {
            if (userRepository.findByContact(newContact) == null || newContact.equals(doctor.getContact())) {
                doctor.setContact(newContact);
            } else {
                return false; // 新的联系方式已经注册
            }
        }
        if (newAddress != null && !newAddress.isEmpty()) {
            doctor.setAddress(newAddress);
        }
        if (newIdNumber != null && !newIdNumber.isEmpty()) {
            doctor.setIdNumber(newIdNumber);
        }

        userRepository.save(doctor);
        return true;
    }

    /**
     * 获取所有患者的概览信息
     *
     * @return 患者概览信息列表
     */
    public List<PatientOverviewDTO> getAllPatients() {
        List<User> users = userRepository.findAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return users.stream()
                .filter(user -> user.getUserType().equals("患者")) // 只过滤出患者
                .map(user -> {
                    PatientOverviewDTO dto = new PatientOverviewDTO();
                    dto.setUsername(user.getUsername());
                    dto.setName(user.getName());
                    dto.setContact(user.getContact());
                    dto.setStatus("有效".equals(user.getStatus()));
                    dto.setRegistrationDate(user.getRegistrationTime().format(formatter));
                    return dto;
                }).collect(Collectors.toList());
    }

    /**
     * 根据用户账号查询患者详细信息
     *
     * @param username 用户账号
     * @return 患者详细信息DTO
     */
    public PatientDetailDTO getPatientDetail(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null || !"患者".equals(user.getUserType())) {
            return null;
        }

        PatientDetailDTO dto = new PatientDetailDTO();
        dto.setName(user.getName());
        dto.setGender(user.getGender());
        dto.setAge(user.getAge());
        dto.setContact(user.getContact());
        dto.setAddress(user.getAddress());
        dto.setIdNumber(user.getIdNumber());

        return dto;
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
     * @return 是否修改成功
     */
    public boolean updatePatientInfo(String username, String newName, String newGender, Integer newAge,
                                     String newContact, String newAddress, String newIdNumber) {
        User user = userRepository.findByUsername(username);
        if (user == null || !"患者".equals(user.getUserType())) {
            return false;
        }

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
            if (userRepository.findByContact(newContact) == null || newContact.equals(user.getContact())) {
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

    /**
     * 根据管理员账号查询基础信息
     *
     * @param username 管理员账号
     * @return 管理员基础信息DTO
     */
    public AdminDetailDTO getAdminDetail(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null || !"管理员".equals(user.getUserType())) {
            return null;
        }

        AdminDetailDTO dto = new AdminDetailDTO();
        dto.setName(user.getName());
        dto.setGender(user.getGender());
        dto.setAge(user.getAge());
        dto.setContact(user.getContact());
        dto.setAddress(user.getAddress());
        dto.setIdNumber(user.getIdNumber());

        return dto;
    }

    /**
     * 修改管理员密码
     *
     * @param username 用户账号
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    public boolean changeAdminPassword(String username, String oldPassword, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null || !"管理员".equals(user.getUserType()) || !user.getPassword().equals(oldPassword)) {
            return false;
        }

        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
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
     * @return 是否修改成功
     */
    public boolean updateAdminInfo(String username, String newName, String newGender, Integer newAge,
                                   String newContact, String newAddress, String newIdNumber) {
        User user = userRepository.findByUsername(username);
        if (user == null || !"管理员".equals(user.getUserType())) {
            return false;
        }

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
            if (userRepository.findByContact(newContact) == null || newContact.equals(user.getContact())) {
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




}