package com.example.Boilerplate_JWTBasedAuthentication.dto.respone;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEmployeeProfileResponse {
    private String fullName;
    private String email;
    private String phoneNumber;
    private UpdateEmployeeProfileResponse.DateOfBirth dateOfBirth;
    private String gender;
    private String location;
    private String token;

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
