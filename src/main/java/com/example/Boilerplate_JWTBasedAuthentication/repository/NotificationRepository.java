package com.example.Boilerplate_JWTBasedAuthentication.repository;

import com.example.Boilerplate_JWTBasedAuthentication.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
