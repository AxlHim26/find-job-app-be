package com.example.Boilerplate_JWTBasedAuthentication.dto.respone;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class JobDetailResponse {
    private String imageLink;
    private String recruiterName;
    private String recruiterMail;
    private String jobTitle;
    private String location;
    private String description;
    private String requirement;
    private String position;
    private String qualification;
    private String experience;
    private String jobType;
    private String salary;
}
