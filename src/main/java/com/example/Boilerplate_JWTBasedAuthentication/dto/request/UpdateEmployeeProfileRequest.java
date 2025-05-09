package com.example.Boilerplate_JWTBasedAuthentication.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeProfileRequest {
    private String fullName;
    private String email;
    private String phoneNumber;
    private UpdateEmployeeProfileRequest.DateOfBirth dateOfBirth;
    private String gender;
    private String location;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DateOfBirth {
        private Integer day;
        private Integer month;
        private Integer year;
    }
}
