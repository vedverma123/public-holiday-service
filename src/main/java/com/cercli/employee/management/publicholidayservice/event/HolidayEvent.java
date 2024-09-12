package com.cercli.employee.management.publicholidayservice.event;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HolidayEvent {
    final String employeeEmail;
    final String location;
}
