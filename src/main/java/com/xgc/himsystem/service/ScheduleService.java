package com.xgc.himsystem.service;

import com.xgc.himsystem.entity.Schedule;
import com.xgc.himsystem.entity.ScheduleDTO;
import com.xgc.himsystem.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    /**
     * 获取所有出诊信息
     *
     * @return 出诊信息列表
     */
    public List<ScheduleDTO> getAllSchedules() {
        // 从数据库获取所有出诊信息
        List<Schedule> schedules = scheduleRepository.findAll();
        LocalDate today = LocalDate.now();

        // 对出诊信息进行排序和转换
        return schedules.stream()
                .sorted((s1, s2) -> {
                    // 如果日期相同，将未发布的放在前面
                    if (s1.getDate().equals(s2.getDate())) {
                        return Boolean.compare(s1.isPublished(), s2.isPublished());
                    }
                    // 按日期排序
                    return s1.getDate().compareTo(s2.getDate());
                })
                .map(schedule -> {
                    ScheduleDTO dto = new ScheduleDTO();
                    dto.setScheduleNumber(schedule.getScheduleNumber());
                    dto.setDepartment(schedule.getDoctor().getDepartment());
                    dto.setDoctorName(schedule.getDoctor().getName());
                    dto.setDate(schedule.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
                    // 根据当前日期判断出诊状态
                    if (schedule.getDate().isAfter(today)) {
                        dto.setStatus("待出诊");
                    } else {
                        dto.setStatus("未出诊");
                    }
                    dto.setPublished(schedule.isPublished());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * 处理出诊单的审核和删除
     *
     * @param scheduleNumber 出诊单号
     * @param auditResult 审核结果
     * @return 处理结果
     */
    public boolean reviewSchedule(String scheduleNumber, boolean auditResult) {
        try {
            Long scheduleId = Long.parseLong(scheduleNumber);
            Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
            if (optionalSchedule.isPresent()) {
                Schedule schedule = optionalSchedule.get();
                if (auditResult) {
                    schedule.setPublished(true);
                    scheduleRepository.save(schedule);
                } else {
                    scheduleRepository.delete(schedule);
                }
                return true;
            }
        } catch (NumberFormatException e) {
            // 处理转换异常
            return false;
        }
        return false;
    }
}
