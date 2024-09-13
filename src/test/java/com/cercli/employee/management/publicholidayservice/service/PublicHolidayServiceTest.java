package com.cercli.employee.management.publicholidayservice.service;

import com.cercli.employee.management.publicholidayservice.dto.EmployeeDto;
import com.cercli.employee.management.publicholidayservice.dto.PublicHolidayDto;
import com.cercli.employee.management.publicholidayservice.entity.PublicHolidayEntity;
import com.cercli.employee.management.publicholidayservice.event.HolidayEvent;
import com.cercli.employee.management.publicholidayservice.exception.PublicHolidayException;
import com.cercli.employee.management.publicholidayservice.mapper.PublicHolidayMapper;
import com.cercli.employee.management.publicholidayservice.repository.PublicHolidayRepository;
import feign.EmployeeServiceFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PublicHolidayServiceTest {

    @Mock
    private PublicHolidayRepository repository;

    @Mock
    private PublicHolidayMapper publicHolidayMapper;

    @Mock
    private EmployeeServiceFeignClient employeeServiceFeignClient;

    @Mock
    private ExternalHolidayService externalHolidayService;

    @Mock
    private EmailService emailService;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private PublicHolidayService publicHolidayService;

    private UUID employeeId;
    private String location;
    private EmployeeDto employeeDto;
    private PublicHolidayEntity holidayEntity;
    private PublicHolidayDto holidayDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employeeId = UUID.randomUUID();
        location = "NYC";

        employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(employeeId);
        employeeDto.setLocation(location);
        employeeDto.setEmail("employee@example.com");

        holidayEntity = new PublicHolidayEntity();
        holidayDto = new PublicHolidayDto();
    }

    @Test
    void testGetUpcomingHolidays_Success() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        // Mock repository response
        when(repository.findByLocationAndHolidayDateBetween(location, today, nextWeek))
                .thenReturn(List.of(holidayEntity));

        when(publicHolidayMapper.mapToDto(holidayEntity)).thenReturn(holidayDto);

        // Execution
        List<PublicHolidayDto> holidays = publicHolidayService.getUpcomingHolidays(location);

        // Verification
        assertFalse(holidays.isEmpty());
        verify(repository).findByLocationAndHolidayDateBetween(location, today, nextWeek);
        verify(publicHolidayMapper).mapToDto(holidayEntity);
    }

    @Test
    void testGetUpcomingHolidays_EmptyList() {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        // Mock repository response to return an empty list
        when(repository.findByLocationAndHolidayDateBetween(location, today, nextWeek))
                .thenReturn(new ArrayList<>());

        // Execution
        List<PublicHolidayDto> holidays = publicHolidayService.getUpcomingHolidays(location);

        // Verification
        assertTrue(holidays.isEmpty());
        verify(repository).findByLocationAndHolidayDateBetween(location, today, nextWeek);
    }

    @Test
    void testGetUpcomingHolidaysForEmployee_Success() {
        // Mock the EmployeeServiceFeignClient response
        when(employeeServiceFeignClient.getEmployeeResidenceLocation(employeeId))
                .thenReturn(new ResponseEntity<>(employeeDto, HttpStatus.OK));

        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusDays(7);

        // Mock repository and mapper behavior
        when(repository.findByLocationAndHolidayDateBetween(location, today, nextWeek))
                .thenReturn(List.of(holidayEntity));
        when(publicHolidayMapper.mapToDto(holidayEntity)).thenReturn(holidayDto);

        // Execution
        List<PublicHolidayDto> holidays = publicHolidayService.getUpcomingHolidaysForEmployee(employeeId);

        // Verification
        assertNotNull(holidays);
        assertFalse(holidays.isEmpty());
        verify(employeeServiceFeignClient).getEmployeeResidenceLocation(employeeId);
        verify(repository).findByLocationAndHolidayDateBetween(location, today, nextWeek);
        verify(publicHolidayMapper).mapToDto(holidayEntity);
    }

    @Test
    void testGetUpcomingHolidaysForEmployee_ThrowsExceptionWhenEmployeeNotFound() {
        // Mock the EmployeeServiceFeignClient to return an error response
        when(employeeServiceFeignClient.getEmployeeResidenceLocation(employeeId))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        // Expect the exception to be thrown
        PublicHolidayException exception = assertThrows(PublicHolidayException.class, () -> {
            publicHolidayService.getUpcomingHolidaysForEmployee(employeeId);
        });

        assertEquals("Employee doesn't exist for the id : " + employeeId, exception.getMessage());
        verify(employeeServiceFeignClient).getEmployeeResidenceLocation(employeeId);
    }

    @Test
    void testSendUpcomingHolidayAlertsForEmployee_ThrowsExceptionWhenEmployeeNotFound() {
        // Mock the EmployeeServiceFeignClient to return an error response
        when(employeeServiceFeignClient.getEmployeeResidenceLocation(employeeId))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        // Expect the exception to be thrown
        PublicHolidayException exception = assertThrows(PublicHolidayException.class, () -> {
            publicHolidayService.sendUpcomingHolidayAlertsForEmployee(employeeId);
        });

        assertEquals("Employee doesn't exist for the id : " + employeeId, exception.getMessage());
        verify(employeeServiceFeignClient).getEmployeeResidenceLocation(employeeId);
    }

    @Test
    void testPublishHolidayEvent_Success() {
        String employeeEmail = "employee@example.com";

        // Execution
        publicHolidayService.publishHolidayEvent(employeeEmail, location);

        // Verification
        verify(applicationEventPublisher).publishEvent(any(HolidayEvent.class));
    }
}
