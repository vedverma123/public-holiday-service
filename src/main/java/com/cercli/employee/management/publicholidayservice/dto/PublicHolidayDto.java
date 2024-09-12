package com.cercli.employee.management.publicholidayservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicHolidayDto {
    Long id;

    @NotBlank
    String location;

    @NotNull
    LocalDate holidayDate;

    String description;
}
