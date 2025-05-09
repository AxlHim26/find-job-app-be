package com.example.Boilerplate_JWTBasedAuthentication.controller;

import com.example.Boilerplate_JWTBasedAuthentication.dto.common.RestResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.EmployeeProfileDTO;
import com.example.Boilerplate_JWTBasedAuthentication.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<RestResponse<EmployeeProfileDTO>> getEmployeeProfile(){
        // Lấy ra người dùng đang thực hiện request
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeService.getEmployeeProfile(username));
    }
}
