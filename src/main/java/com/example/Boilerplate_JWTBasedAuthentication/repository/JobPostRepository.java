package com.example.Boilerplate_JWTBasedAuthentication.repository;

import com.example.Boilerplate_JWTBasedAuthentication.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Date;

public interface JobPostRepository extends JpaRepository<JobPost, Integer> {
    @Query("SELECT j FROM JobPost j ORDER BY j.createdAt DESC")
    List<JobPost> findTop5ByOrderByCreatedAtDesc();

    JobPost findJobPostsById(int id);

    @Query("SELECT j FROM JobPost j WHERE " +
            "(:title = 'All' OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:location = 'All' OR j.recruiter.location LIKE %:location%) AND " +
            "(:position = 'All' OR j.position LIKE %:position%) AND " +
            "(:experience = 'All' OR j.experience LIKE %:experience%) AND " +
            "(:salary = 'All' OR j.salary LIKE %:salary%) AND " +
            "j.expirateAt > CURRENT_DATE")
    List<JobPost> searchJobs(
            @Param("title") String title,
            @Param("location") String location,
            @Param("position") String position,
            @Param("experience") String experience,
            @Param("salary") String salary
    );
}
