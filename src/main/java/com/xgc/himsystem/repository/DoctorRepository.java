package com.xgc.himsystem.repository;

import com.xgc.himsystem.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByUsername(String username);
}
