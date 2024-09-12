package com.cercli.employee.management.publicholidayservice.mapper;

import com.cercli.employee.management.publicholidayservice.dto.PublicHolidayDto;
import com.cercli.employee.management.publicholidayservice.entity.PublicHolidayEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublicHolidayMapper {

    PublicHolidayDto mapToDto(PublicHolidayEntity source);

    PublicHolidayEntity mapToEntity(PublicHolidayDto source);
}
