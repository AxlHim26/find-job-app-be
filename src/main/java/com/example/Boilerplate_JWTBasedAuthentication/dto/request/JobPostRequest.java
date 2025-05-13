package com.example.Boilerplate_JWTBasedAuthentication.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class JobPostRequest {
    private String title;
    private String description;
    private String requirement;
    private String position;
    private String qualification;
    private String experience;
    private String type;
    private String workplaceType;
    private String salary;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy hh:mm:ss a", locale = "en")
    private Date expirateAt;
}
