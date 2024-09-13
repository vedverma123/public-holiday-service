package com.cercli.employee.management.publicholidayservice.repository;

import com.cercli.employee.management.publicholidayservice.entity.PublicHolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface PublicHolidayRepository extends JpaRepository<PublicHolidayEntity, Long> {
    List<PublicHolidayEntity> findByLocationAndHolidayDateBetween(String location, LocalDate startDate, LocalDate endDate);

}
