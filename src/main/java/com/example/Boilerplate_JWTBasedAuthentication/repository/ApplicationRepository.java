package com.example.Boilerplate_JWTBasedAuthentication.repository;

import com.example.Boilerplate_JWTBasedAuthentication.entity.Application;
import com.example.Boilerplate_JWTBasedAuthentication.entity.Employee;
import com.example.Boilerplate_JWTBasedAuthentication.entity.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    Application findByEmployeeAndJobPost(Employee employee, JobPost jobPost);
}
