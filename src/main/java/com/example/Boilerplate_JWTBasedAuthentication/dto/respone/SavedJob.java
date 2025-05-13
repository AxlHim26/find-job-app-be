package com.example.Boilerplate_JWTBasedAuthentication.dto.respone;

import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SavedJob {
    private int id;
    private String imageUrl;
    private String jobTitle;
    private String companyName;
    private String location;
    private String jobPosition;
    private String jobType;
    private Date createdAt;
    private String salary;
}
