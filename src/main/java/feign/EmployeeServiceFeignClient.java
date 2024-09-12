package feign;

import com.cercli.employee.management.publicholidayservice.dto.EmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(
        name = "employeeservice",
        url = "employeeservice" //provide the url for employee-service here.
)
public interface EmployeeServiceFeignClient {
    ResponseEntity<EmployeeDto> getEmployeeResidenceLocation(@RequestParam UUID employeeId);
}
