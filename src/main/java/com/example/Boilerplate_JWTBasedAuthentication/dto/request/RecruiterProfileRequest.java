package com.example.Boilerplate_JWTBasedAuthentication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RecruiterProfileRequest {
    private String about;
    private String website;
    private String industry;
    private String location;
    private String since;
    private String specialization;
}
