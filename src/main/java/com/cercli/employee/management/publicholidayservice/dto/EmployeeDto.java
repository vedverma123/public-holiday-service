package com.cercli.employee.management.publicholidayservice.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeDto {
    UUID employeeId;
    String location;
    String email;
}
