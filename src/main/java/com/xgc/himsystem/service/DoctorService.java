package com.xgc.himsystem.service;

import com.xgc.himsystem.entity.Doctor;
import com.xgc.himsystem.entity.DoctorInfoDTO;
import com.xgc.himsystem.entity.Schedule;
import com.xgc.himsystem.entity.ScheduleDTO;
import com.xgc.himsystem.repository.DoctorRepository;
import com.xgc.himsystem.repository.ScheduleRepository;
import com.xgc.himsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    public String publishSchedule(String doctorUsername, String date, int availableRegisterNum,
                                  boolean isRegular, String frequency) {
        // 根据用户名查找医生
        Doctor doctor = doctorRepository.findByUsername(doctorUsername);
        if (doctor == null) {
            return "Doctor not found";
        }

        // 解析出诊日期并确定结束日期为一年后
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startDate = LocalDate.parse(date, formatter);
        LocalDate endDate = startDate.plusYears(1);
        boolean conflict = false;

        // 循环处理定期出诊信息，直到结束日期
        while (!startDate.isAfter(endDate)) {
            // 检查该医生在该日期是否已有出诊安排
            if (scheduleRepository.findByDoctorAndDate(doctor, startDate).isEmpty()) {
                // 如果没有冲突，生成出诊单号并保存出诊信息
                String scheduleNumber = UUID.randomUUID().toString();
                Schedule schedule = new Schedule(scheduleNumber, startDate, availableRegisterNum, doctor);
                scheduleRepository.save(schedule);
            } else {
                // 如果存在冲突，标记冲突
                conflict = true;
            }

            // 如果不是定期出诊，跳出循环
            if (!isRegular) {
                break;
            }

            // 根据频率更新出诊日期
            switch (frequency) {
                case "每日":
                    startDate = startDate.plusDays(1);
                    break;
                case "每周":
                    startDate = startDate.plusWeeks(1);
                    break;
                case "每月":
                    startDate = startDate.plusMonths(1);
                    break;
                default:
                    return "Invalid frequency";
            }
        }

        // 根据是否存在冲突返回相应的信息
        return conflict ? "Some schedules were not added due to conflicts" : "Success";
    }


    /**
     * 获取医生的所有出诊信息
     *
     * @param doctorUsername 医生的用户名
     * @return 包含医生出诊信息的列表
     */
    public List<com.xgc.himsystem.entity.ScheduleDTO> getDoctorSchedules(String doctorUsername) {
        Doctor doctor = doctorRepository.findByUsername(doctorUsername);
        if (doctor == null) {
            return null;
        }

        // 查找医生的所有出诊信息
        List<Schedule> schedules = scheduleRepository.findByDoctor(doctor);
        // 获取当前日期
        LocalDate today = LocalDate.now();

        // 将出诊信息转换为ScheduleDTO并返回
        return schedules.stream().map(schedule -> {
            ScheduleDTO dto = new ScheduleDTO();
            // 设置出诊单号
            dto.setScheduleNumber(schedule.getScheduleNumber());
            // 设置医生所在科室
            dto.setDepartment(doctor.getDepartment());
            // 设置医生姓名
            dto.setDoctorName(doctor.getName());
            // 设置出诊日期，格式为yyyyMMdd
            dto.setDate(schedule.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            // 根据出诊日期和当前日期判断出诊状态
            if (schedule.getDate().isAfter(today)) {
                dto.setStatus("待出诊");
            } else {
                dto.setStatus("未出诊");
            }
            // 设置发布情况
            dto.setPublished(schedule.isPublished());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 根据日期范围查询医生的出诊信息
     *
     * @param doctorUsername 医生的用户名
     * @param startDate 起始日期，格式为yyyyMMdd
     * @param endDate 截止日期，格式为yyyyMMdd
     * @return 包含医生出诊信息的列表
     */
    public List<ScheduleDTO> getDoctorSchedulesByDateRange(String doctorUsername, String startDate, String endDate) {
        // 根据用户名查找医生
        Doctor doctor = doctorRepository.findByUsername(doctorUsername);
        if (doctor == null) {
            // 如果医生不存在，返回null
            return null;
        }

        // 解析日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        // 获取当前日期
        LocalDate today = LocalDate.now();

        // 查找指定日期范围内的出诊信息
        List<Schedule> schedules = scheduleRepository.findByDoctorAndDateBetween(doctor, start, end);

        // 将出诊信息转换为ScheduleDTO并返回
        return schedules.stream().map(schedule -> {
            ScheduleDTO dto = new ScheduleDTO();
            // 设置出诊单号
            dto.setScheduleNumber(schedule.getScheduleNumber());
            // 设置医生所在科室
            dto.setDepartment(doctor.getDepartment());
            // 设置医生姓名
            dto.setDoctorName(doctor.getName());
            // 设置出诊日期，格式为yyyyMMdd
            dto.setDate(schedule.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            // 根据出诊日期和当前日期判断出诊状态
            if (schedule.getDate().isAfter(today)) {
                dto.setStatus("待出诊");
            } else {
                dto.setStatus("未出诊");
            }
            // 设置发布情况
            dto.setPublished(schedule.isPublished());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取医生的基础信息
     *
     * @param doctorUsername 医生的用户名
     * @return 医生的基础信息DTO
     */
    public DoctorInfoDTO getDoctorInfo(String doctorUsername) {
        Doctor doctor = doctorRepository.findByUsername(doctorUsername);
        if (doctor == null) {
            return null;
        }

        DoctorInfoDTO dto = new DoctorInfoDTO();
        dto.setUserType("医生");
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

    public boolean updateDoctorInfo(String doctorUsername, String newName, String newSpecialty, String newHospital,
                                    String newDepartment, String newTitle, String newGender, Integer newAge,
                                    String newContact, String newAddress, String newIdCard) {
        Doctor doctor = doctorRepository.findByUsername(doctorUsername);
        if (doctor == null) {
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
            if (userRepository.findByContact(newContact) == null) {
                doctor.setContact(newContact);
            } else {
                return false; // 新的联系方式已经注册
            }
        }
        if (newAddress != null && !newAddress.isEmpty()) {
            doctor.setAddress(newAddress);
        }
        if (newIdCard != null && !newIdCard.isEmpty()) {
            doctor.setIdNumber(newIdCard);
        }

        doctorRepository.save(doctor);
        return true;
    }

    public boolean changeDoctorPassword(String doctorUsername, String oldPassword, String newPassword) {
        Doctor doctor = doctorRepository.findByUsername(doctorUsername);
        if (doctor == null) {
            return false;
        }

        if (!doctor.getPassword().equals(oldPassword)) {
            return false;
        }

        doctor.setPassword(newPassword);
        doctorRepository.save(doctor);
        return true;
    }

    public boolean registerDoctor(String name, String gender, int age, String contact, String address,
                                  String idCard, String password, String specialty, String hospital, String department,
                                  String title) {
        if (userRepository.findByContact(contact) != null) {
            return false;
        }
        String username = generateUsername();
        Doctor newDoctor = new Doctor(username, password, "医生", name, gender, age, contact, address, idCard,
                "无效", LocalDateTime.now(), specialty, hospital, department, title);
        doctorRepository.save(newDoctor);
        return true;
    }

    public Doctor loginDoctor(String username, String password) {
        Doctor doctor = doctorRepository.findByUsername(username);
        if (doctor != null && doctor.getPassword().equals(password) && "有效".equals(doctor.getStatus())) {
            return doctor;
        }
        return null;
    }

    private String generateUsername() {
        int prefix = 900;
        return String.valueOf(prefix) + String.valueOf((int) (1000000 + Math.random() * 9000000));
    }

    public List<Schedule> findAvailableDoctors(String date, String department) {
        // 将字符串日期转换为LocalDate
        LocalDate localDate = LocalDate.parse(date);
        // 查询指定日期和科室的医生排班信息
        List<Schedule> schedules = scheduleRepository.findByDateAndDoctor_Department(localDate, department);
        // 过滤出可挂号数大于0的记录
        return schedules.stream()
                .filter(schedule -> schedule.getAvailableRegisterNum() > 0)
                .collect(Collectors.toList());
    }
}
