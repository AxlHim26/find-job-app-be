package com.example.Boilerplate_JWTBasedAuthentication.repository;

import com.example.Boilerplate_JWTBasedAuthentication.entity.PasswordResetToken;
import com.example.Boilerplate_JWTBasedAuthentication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUser(User user);
    void deleteByUser(User user);
}
