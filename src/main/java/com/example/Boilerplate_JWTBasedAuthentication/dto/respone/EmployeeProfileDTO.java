package com.example.Boilerplate_JWTBasedAuthentication.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeProfileDTO {
    private String fullName;
    private String email;
    private String phoneNumber;
    private DateOfBirth dateOfBirth;
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
