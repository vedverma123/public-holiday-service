package com.cercli.employee.management.publicholidayservice.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "public_holiday")
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublicHolidayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    Long id;

    @Column(nullable = false)
    String location;

    @Column(name = "holiday_date", nullable = false)
    LocalDate holidayDate;

    String description;

}
