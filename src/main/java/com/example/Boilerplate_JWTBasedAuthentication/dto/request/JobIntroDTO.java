package com.example.Boilerplate_JWTBasedAuthentication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class JobIntroDTO {
    private int id;
    private String title;
    private String salary;
    private String position;
    private String jobType;
    private Date createdAt;
    private boolean isSaved;
}
