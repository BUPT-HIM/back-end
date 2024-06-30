package com.xgc.himsystem.service;

import com.xgc.himsystem.entity.RegisterOrder;
import com.xgc.himsystem.repository.RegisterOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegisterOrderService {
    @Autowired
    private RegisterOrderRepository registerOrderRepository;

    private static final Logger logger = LoggerFactory.getLogger(RegisterOrderService.class);

    public void register(String date, String department, String doctorName) {
        // 使用自定义日期格式解析字符串日期
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        // 生成唯一订单编号
        String orderNumber = UUID.randomUUID().toString();
        // 创建订单
        RegisterOrder order = new RegisterOrder(orderNumber, localDate, department, doctorName,
                                LocalDateTime.now(), "未预约", "待就诊");

        // 存储订单
        registerOrderRepository.save(order);
    }

    public void updateAppointmentStatus(String orderNumber, String appointmentStatus) {
        // 获取当前时间
        LocalTime now = LocalTime.now();
        // 检查预约时间是否合法
        if ((appointmentStatus.equals("当日上午") && (now.isBefore(LocalTime.of(7, 0)) || now.isAfter(LocalTime.of(9, 0)))) ||
                (appointmentStatus.equals("当日下午") && now.isBefore(LocalTime.of(7, 0)) || now.isAfter(LocalTime.of(14, 0)))) {
            throw new IllegalArgumentException("不合法的预约时间");
        }

        // 查询订单
        Optional<RegisterOrder> optionalOrder = registerOrderRepository.findByOrderNumber(orderNumber);
        if (optionalOrder.isPresent()) {
            RegisterOrder order = optionalOrder.get();
            // 更新预约情况
            order.setAppointmentStatus(appointmentStatus);
            // 保存订单
            registerOrderRepository.save(order);
        }
    }

    public List<RegisterOrder> findOrdersByDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        List<RegisterOrder> orders = registerOrderRepository.findByDate(localDate);
        if (orders.isEmpty()) {
            logger.info("当日无订单: {}", date);
        }
        return orders;
    }

    public List<RegisterOrder> findOrdersByDateRange(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate startLocalDate = LocalDate.parse(startDate, formatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, formatter);
        List<RegisterOrder> orders = registerOrderRepository.findByDateBetween(startLocalDate, endLocalDate);
        if (orders.isEmpty()) {
            logger.info("在指定日期范围内无订单: {} - {}", startDate, endDate);
        }
        return orders;
    }

    public List<RegisterOrder> findAllOrders() {
        return registerOrderRepository.findAll();
    }
}
