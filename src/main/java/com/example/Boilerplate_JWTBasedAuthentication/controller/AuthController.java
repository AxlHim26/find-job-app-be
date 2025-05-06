package com.example.Boilerplate_JWTBasedAuthentication.controller;

import com.example.Boilerplate_JWTBasedAuthentication.dto.common.RestResponse;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.LoginRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.RegisterRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.AuthResponse;
import com.example.Boilerplate_JWTBasedAuthentication.exception.custome.RoleNotFoundException;
import com.example.Boilerplate_JWTBasedAuthentication.exception.custome.UsernameExistedException;
import com.example.Boilerplate_JWTBasedAuthentication.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth Controller", description = "API for login and register")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register by email & password", description = "Create a user base on email & password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully create a user"),
            @ApiResponse(responseCode = "409", description = "This email is used for another account")
    })
    public ResponseEntity<RestResponse<Void>> register(
            @Parameter(description = "Register info - email & password", required = true)
            @RequestBody RegisterRequest request
    ) throws UsernameExistedException, RoleNotFoundException {

        log.info(" name: = " + request.getName() +
                ", email = " + request.getEmail() +
                ", pass = " + request.getPassword() +
                ", role = " + request.getRole());

        authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                RestResponse.success("User registered successfully")
        );
    }


    @PostMapping("/login")
    @Operation(
            summary = "Login by email and password",
            description = "Authenticate a user using email and password. Returns access and refresh tokens if successful."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = "Email not found")
    })
    public ResponseEntity<RestResponse<AuthResponse>> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        AuthResponse authResponse = authService.login(request);

        return ResponseEntity.status(HttpStatus.OK).body(
                RestResponse.success(authResponse, "User logged in successfully")
        );
    }
}
