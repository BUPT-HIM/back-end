package com.xgc.himsystem.repository;

import com.xgc.himsystem.entity.Doctor;
import com.xgc.himsystem.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByDateAndDoctor_Department(LocalDate date, String department);
    List<Schedule> findByDoctorAndDate(Doctor doctor, LocalDate date);
    List<Schedule> findByDoctor(Doctor doctor);
    List<Schedule> findByDoctorAndDateBetween(Doctor doctor, LocalDate startDate, LocalDate endDate);
}
