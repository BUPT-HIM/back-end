package com.xgc.himsystem.controller;

import com.xgc.himsystem.entity.RegisterOrder;
import com.xgc.himsystem.service.RegisterOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/registerOrder")
public class RegisterOrderController {
    @Autowired
    private RegisterOrderService registerOrderService;

    @PostMapping("/register")
    public void register(@RequestParam String date, @RequestParam String department, @RequestParam String doctorName) {
        registerOrderService.register(date, department, doctorName);
    }

    @PostMapping("/update-appointment")
    public void updateAppointmentStatus(@RequestParam String orderNumber, @RequestParam String appointmentStatus) {
        registerOrderService.updateAppointmentStatus(orderNumber, appointmentStatus);
    }

    @GetMapping("/orders")
    public Object findOrdersByDate(@RequestParam String date) {
        List<RegisterOrder> orders = registerOrderService.findOrdersByDate(date);
        if (orders.isEmpty()) {
            return Collections.emptyList();
        } else {
            return orders;
        }
    }

    @GetMapping("/all-orders")
    public List<RegisterOrder> findAllOrders() {
        return registerOrderService.findAllOrders();
    }

    @GetMapping("/orders-by-date-range")
    public List<RegisterOrder> findOrdersByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        return registerOrderService.findOrdersByDateRange(startDate, endDate);
    }
}