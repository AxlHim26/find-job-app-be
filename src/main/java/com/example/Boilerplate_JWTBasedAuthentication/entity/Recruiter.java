package com.example.Boilerplate_JWTBasedAuthentication.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "recruiter")
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "about",  columnDefinition = "LONGTEXT")
    private String about;

    @Column(name = "website")
    private String website;

    @Column(name = "industry")
    private String industry;

    @Column(name = "location")
    private String location;

    @Column(name = "since")
    private String since;

    @Column(name = "specialization", columnDefinition = "LONGTEXT")
    private String specialization;

    @Column(name = "avatar_link", nullable = true)
    private String avatarLink;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, orphanRemoval = true)
    private List<JobPost> jobPosts;
}
