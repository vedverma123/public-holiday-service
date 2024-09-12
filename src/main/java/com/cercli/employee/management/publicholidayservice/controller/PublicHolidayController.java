package com.cercli.employee.management.publicholidayservice.controller;

import com.cercli.employee.management.publicholidayservice.dto.PublicHolidayDto;
import com.cercli.employee.management.publicholidayservice.service.PublicHolidayService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/holidays")
@RequiredArgsConstructor
public class PublicHolidayController {
    PublicHolidayService publicHolidayService;

    @GetMapping("/upcoming")
    public ResponseEntity<List<PublicHolidayDto>> getUpcomingHolidays(@RequestParam String location) {
        return ResponseEntity.ok(publicHolidayService.getUpcomingHolidays(location));
    }

    @PostMapping("/send-alerts")
    public void sendHolidayAlerts(@RequestParam UUID employeeId) {
        publicHolidayService.sendUpcomingHolidayAlertsForEmployee(employeeId);
    }
}
