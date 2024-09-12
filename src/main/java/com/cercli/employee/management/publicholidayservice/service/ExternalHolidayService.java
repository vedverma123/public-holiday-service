package com.cercli.employee.management.publicholidayservice.service;

import com.cercli.employee.management.publicholidayservice.dto.PublicHolidayDto;
import com.cercli.employee.management.publicholidayservice.entity.PublicHolidayEntity;
import com.cercli.employee.management.publicholidayservice.mapper.PublicHolidayMapper;
import com.cercli.employee.management.publicholidayservice.repository.PublicHolidayRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ExternalHolidayService {

    RestTemplate restTemplate;
    PublicHolidayMapper holidayMapper;
    PublicHolidayRepository publicHolidayRepository;

    public void fetchAndStoreHolidays(String countryCode) {
        String apiUrl = "https://holidayapi.com/v1/holidays?country=" + countryCode + "&year=" + LocalDate.now().getYear();

        //Assuming this API returns a list of holidays, and we might need to convert it into our PublicHolidayEntity.
        List<PublicHolidayDto> holidays = restTemplate.getForObject(apiUrl, List.class);

        List<PublicHolidayEntity> holidayEntities = holidays.stream().filter(Objects::nonNull).map(holidayMapper::mapToEntity).toList();
        // Store holidays in the database
        publicHolidayRepository.saveAll(holidayEntities);
    }
}
