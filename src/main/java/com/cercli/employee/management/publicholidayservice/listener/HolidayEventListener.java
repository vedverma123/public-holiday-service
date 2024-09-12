package com.cercli.employee.management.publicholidayservice.listener;

import com.cercli.employee.management.publicholidayservice.event.HolidayEvent;
import com.cercli.employee.management.publicholidayservice.service.EmailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class HolidayEventListener {

    EmailService emailService;

    @EventListener
    public void handleHolidayEvent(HolidayEvent holidayEvent) {
        emailService.sendEmail(holidayEvent.getEmployeeEmail(), "Holiday ALert", "Upcoming Holiday");
    }
}
