package com.example.Boilerplate_JWTBasedAuthentication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String role;
    private String name;
    private String email;
    private String password;
}
