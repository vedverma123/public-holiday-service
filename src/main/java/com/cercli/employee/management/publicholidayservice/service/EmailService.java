package com.cercli.employee.management.publicholidayservice.service;

import com.cercli.employee.management.publicholidayservice.dto.PublicHolidayDto;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Async
    public void sendEmail(String emailId, String subject, String body) {
        //sending an email to the employee asynchronously using any mail service.
    }

    @Async
    @Retryable(value = {Exception.class}, maxAttempts = 3)
    public void sendUpcomingHolidayAlert(String employeeEmail, List<PublicHolidayDto> holidays) {
        StringBuilder body = new StringBuilder("Here are the upcoming public holidays:\n");
        for (PublicHolidayDto holiday : holidays) {
            body.append(holiday.getHolidayDate()).append(" - ").append(holiday.getDescription()).append("\n");
        }

        // Simulate sending an email
        System.out.println("Sending email to: " + employeeEmail);
        System.out.println("Body: " + body.toString());
    }
}
