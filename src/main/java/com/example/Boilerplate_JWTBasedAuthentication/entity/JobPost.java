package com.example.Boilerplate_JWTBasedAuthentication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "job_post")
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "position")
    private String position;

    @Column(name = "qualification")
    private String qualification;

    @Column(name = "experience")
    private String experience;

    @Column(name = "type")
    private String type;

    @Column(name = "salary")
    private String salary;

    @Column(name = "expirate_at")
    private Date expirateAt;

    @Column(name = "requirement")
    private String requirement;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter recruiter;

    @Column(name = "workplace_type")
    private String workplaceType;

    @OneToMany(mappedBy = "jobPost", fetch = FetchType.LAZY)
    private List<Application> applications;

    // Constructor tùy chỉnh
    public JobPost(Recruiter recruiter, String title, String description, String requirement, String position, String qualification, String experience, String type, String workplaceType, String salary, Date expirateAt) {
        this.title = title;
        this.description = description;
        this.position = position;
        this.requirement = requirement;
        this.qualification = qualification;
        this.experience = experience;
        this.type = type;
        this.salary = salary;
        this.recruiter = recruiter;
        this.workplaceType = workplaceType;
        this.expirateAt = expirateAt;
    }

    // Tự động gán ngày tạo trước khi persist
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
    }
}

