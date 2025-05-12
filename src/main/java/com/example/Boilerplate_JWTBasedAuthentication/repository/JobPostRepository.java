package com.example.Boilerplate_JWTBasedAuthentication.repository;

import com.example.Boilerplate_JWTBasedAuthentication.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JobPostRepository extends JpaRepository<JobPost, Integer> {
    @Query("SELECT j FROM JobPost j ORDER BY j.createdAt DESC")
    List<JobPost> findTop5ByOrderByCreatedAtDesc();
}
