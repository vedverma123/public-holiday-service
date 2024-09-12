package com.cercli.employee.management.publicholidayservice.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicHolidayFetchScheduler {

    ExternalHolidayService externalHolidayService;

    @Scheduled(cron = "0 0 0 1 * ?")  // Fetch holidays on the 1st of every month
    public void fetchPublicHolidays() {
        externalHolidayService.fetchAndStoreHolidays("US"); // Assuming we're fetching US holidays for now
    }
}
