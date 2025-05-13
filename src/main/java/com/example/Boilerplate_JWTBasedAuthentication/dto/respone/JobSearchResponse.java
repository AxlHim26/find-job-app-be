package com.example.Boilerplate_JWTBasedAuthentication.dto.respone;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobSearchResponse {
    private int id;
    private String imageUrl;
    private String jobTitle;
    private String companyName;
    private String location;
    private String jobPosition;
    private String jobType;
    private String salary;
    private Date createdAt;
    private boolean isSaved;
}
