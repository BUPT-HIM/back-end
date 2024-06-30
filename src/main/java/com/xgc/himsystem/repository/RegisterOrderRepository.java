package com.xgc.himsystem.repository;

import com.xgc.himsystem.entity.RegisterOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RegisterOrderRepository extends JpaRepository<RegisterOrder, Long> {
    Optional<RegisterOrder> findByOrderNumber(String orderNumber);
    List<RegisterOrder> findByDate(LocalDate date);
    List<RegisterOrder> findByDateBetween(LocalDate startDate, LocalDate endDate);
}