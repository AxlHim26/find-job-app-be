package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.dto.common.RestResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.EmployeeProfileDTO;
import com.example.Boilerplate_JWTBasedAuthentication.entity.Employee;
import com.example.Boilerplate_JWTBasedAuthentication.entity.User;
import com.example.Boilerplate_JWTBasedAuthentication.repository.EmployeeRepository;
import com.example.Boilerplate_JWTBasedAuthentication.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    public EmployeeService(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    public RestResponse<EmployeeProfileDTO> getEmployeeProfile(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("Username not found")
        );
        Employee employee = user.getEmployee();

        Date birthDay = employee.getBirthday();
        LocalDate localDate = birthDay.toInstant()
                .atZone(VIETNAM_ZONE)
                .toLocalDate();

        EmployeeProfileDTO.DateOfBirth dateOfBirth = EmployeeProfileDTO.DateOfBirth.builder()
                .day(localDate.getDayOfMonth())
                .month(localDate.getMonthValue())
                .year(localDate.getYear())
                .build();

        EmployeeProfileDTO employeeProfileDTO = EmployeeProfileDTO.builder()
                .fullName(employee.getUser().getName())
                .email(employee.getUser().getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .dateOfBirth(dateOfBirth)
                .gender(employee.getGender() ? "MALE" : "FEMALE")
                .location(employee.getLocation())
                .build();

        return RestResponse.success(
                employeeProfileDTO,
                "Get employee profile success"
        );
    }
}
