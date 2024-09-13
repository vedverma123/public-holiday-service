package com.cercli.employee.management.publicholidayservice.service;

import com.cercli.employee.management.publicholidayservice.dto.EmployeeDto;
import com.cercli.employee.management.publicholidayservice.dto.PublicHolidayDto;
import com.cercli.employee.management.publicholidayservice.entity.PublicHolidayEntity;
import com.cercli.employee.management.publicholidayservice.event.HolidayEvent;
import com.cercli.employee.management.publicholidayservice.exception.PublicHolidayException;
import com.cercli.employee.management.publicholidayservice.mapper.PublicHolidayMapper;
import com.cercli.employee.management.publicholidayservice.repository.PublicHolidayRepository;
import com.cercli.employee.management.publicholidayservice.feign.EmployeeServiceFeignClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PublicHolidayService {

    PublicHolidayRepository repository;
    PublicHolidayMapper publicHolidayMapper;
    EmployeeServiceFeignClient employeeServiceFeignClient;
    ExternalHolidayService externalHolidayService;
    EmailService emailService;
    ApplicationEventPublisher applicationEventPublisher;

    @Cacheable(value = "holidaysCache", key = "#location")
    public List<PublicHolidayDto> getUpcomingHolidays(final String location) {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        return repository.findByLocationAndHolidayDateBetween(location, today, nextWeek)
                .stream()
                .filter(Objects::nonNull)
                .map(publicHolidayMapper::mapToDto)
                .toList();
    }

    public List<PublicHolidayDto> getUpcomingHolidaysForEmployee(UUID employeeId) {
        // Get employee's residence location from the employee service using Feign Client.
        ResponseEntity<EmployeeDto> response = employeeServiceFeignClient.getEmployeeResidenceLocation(employeeId);

        if(Objects.isNull(response) || !response.getStatusCode().is2xxSuccessful()) {
            throw new PublicHolidayException("Employee doesn't exist for the id : " + employeeId);
        }

        EmployeeDto employee = response.getBody();
        // Fetch holidays for this location
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);
        List<PublicHolidayEntity> holidays = repository.findByLocationAndHolidayDateBetween(employee.getLocation(), today, nextWeek);

        // If no holidays found locally, fetch from external API
        if (holidays.isEmpty()) {
            externalHolidayService.fetchAndStoreHolidays(employee.getLocation());
            holidays = repository.findByLocationAndHolidayDateBetween(employee.getLocation(), today, nextWeek);
        }

        return Stream.ofNullable(holidays)
                .flatMap(Collection::stream)
                .map(publicHolidayMapper::mapToDto)
                .toList();
    }


    public void sendUpcomingHolidayAlertsForEmployee(UUID employeeId) {
        // Get employee's residence location from the employee service using Feign Client.
        ResponseEntity<EmployeeDto> response = employeeServiceFeignClient.getEmployeeResidenceLocation(employeeId);

        if(Objects.isNull(response) || !response.getStatusCode().is2xxSuccessful()) {
            throw new PublicHolidayException("Employee doesn't exist for the id : " + employeeId);
        }

        EmployeeDto employee = response.getBody();

        // Fetch upcoming holidays for the employee's location
        List<PublicHolidayDto> upcomingHolidays = getUpcomingHolidaysForEmployee(employeeId);

        // Send email alert to the employee
        emailService.sendUpcomingHolidayAlert(employee.getEmail(), upcomingHolidays);
    }

    public void publishHolidayEvent(String employeeEmail, String location) {
        // Fetch holiday logic
        applicationEventPublisher.publishEvent(new HolidayEvent(employeeEmail, location));
    }
}
