package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.dto.common.RestResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.UpdateEmployeeProfileRequest;
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

    public RestResponse<EmployeeProfileDTO> getEmployeeProfile(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
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

    public UpdateEmployeeProfileRequest updateProfile(Long id, UpdateEmployeeProfileRequest request) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        Employee employee = user.getEmployee();

        user.setName(request.getFullName());
        user.setEmail(request.getEmail());
        employee.setPhoneNumber(request.getPhoneNumber());
        employee.setGender(request.getGender().equals("MALE"));
        employee.setLocation(request.getLocation());

        // Lấy đối tượng birthdate
        LocalDate localBirthDay = LocalDate.of(
                request.getDateOfBirth().getYear(),
                request.getDateOfBirth().getMonth(),
                request.getDateOfBirth().getDay()
        );

        Date birthDay = Date.from(localBirthDay.atStartOfDay(VIETNAM_ZONE).toInstant());
        employee.setBirthday(birthDay);

        // Lưu employee và user vào DB
        employeeRepository.save(employee);
        userRepository.save(user);

        return request;
    }
}
