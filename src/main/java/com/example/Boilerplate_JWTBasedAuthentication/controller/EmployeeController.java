package com.example.Boilerplate_JWTBasedAuthentication.controller;

import com.example.Boilerplate_JWTBasedAuthentication.dto.common.RestResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.UpdateEmployeeProfileRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.EmployeeProfileDTO;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.UpdateEmployeeProfileResponse;
import com.example.Boilerplate_JWTBasedAuthentication.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<RestResponse<EmployeeProfileDTO>> getEmployeeProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeService.getEmployeeProfile(username));
    }

    @PostMapping("/profile")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<RestResponse<UpdateEmployeeProfileResponse>> updateProfile(
            @RequestBody UpdateEmployeeProfileRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                RestResponse.success(
                        employeeService.updateProfile(username, request),
                        "Update profile successfully"
                )
        );
    }
}
