package com.example.Boilerplate_JWTBasedAuthentication.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangePasswordRequest {
    private String current;
    private String newPassword;
    private String confirmPassword;
}
