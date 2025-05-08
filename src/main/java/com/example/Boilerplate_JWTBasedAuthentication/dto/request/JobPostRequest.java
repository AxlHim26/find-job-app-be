package com.example.Boilerplate_JWTBasedAuthentication.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class JobPostRequest {
    private String title;
    private String description;
    private String position;
    private String qualification;
    private String experience;
    private String type;
    private String salary;
    private Date expirateAt;
}
