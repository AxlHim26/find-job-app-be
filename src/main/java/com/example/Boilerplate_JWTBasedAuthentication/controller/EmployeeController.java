package com.example.Boilerplate_JWTBasedAuthentication.controller;

import com.example.Boilerplate_JWTBasedAuthentication.dto.common.RestResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.ChangeImageRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.ChangePasswordRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.UpdateEmployeeProfileRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.EmployeeProfileDTO;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.UpdateEmployeeProfileResponse;
import com.example.Boilerplate_JWTBasedAuthentication.exception.custome.WrongCurrentPasswordException;
import com.example.Boilerplate_JWTBasedAuthentication.service.EmployeeService;
import com.example.Boilerplate_JWTBasedAuthentication.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/api/employee")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final UserService userService;

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

    @PostMapping("/change-password")
    public ResponseEntity<RestResponse<Void>> changePassword(
            @RequestBody ChangePasswordRequest request
    ) throws WrongCurrentPasswordException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // đổi mk
        userService.changePassword(username, request);

        return ResponseEntity.status(HttpStatus.OK).body(
                RestResponse.success(
                        "Change password successfully"
                )
        );
    }

    @GetMapping("/avatar")
    public ResponseEntity<RestResponse<String>> getProfileImage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ResponseEntity.status(HttpStatus.OK).body(
                RestResponse.success(
                        employeeService.getProfileImage(username),
                        "Get profile image success"
                )
        );
    }

    @PostMapping("/change-avatar")
    public ResponseEntity<RestResponse<String>> changeProfileImage(
            @RequestBody ChangeImageRequest request
            ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        log.debug("Changing image of " + username);


        return ResponseEntity.status(HttpStatus.CREATED).body(
                RestResponse.success(
                        employeeService.changeProfileImage(username, request),
                        "Change image success"
                )
        );
    }
}
