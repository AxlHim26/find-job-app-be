package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.dto.request.LoginRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.request.RegisterRequest;
import com.example.Boilerplate_JWTBasedAuthentication.dto.respone.AuthResponse;
import com.example.Boilerplate_JWTBasedAuthentication.entity.Role;
import com.example.Boilerplate_JWTBasedAuthentication.entity.User;
import com.example.Boilerplate_JWTBasedAuthentication.exception.custome.*;
import com.example.Boilerplate_JWTBasedAuthentication.repository.RoleRepository;
import com.example.Boilerplate_JWTBasedAuthentication.repository.UserRepository;
import com.example.Boilerplate_JWTBasedAuthentication.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequest request) throws UsernameExistedException, RoleNotFoundException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new UsernameExistedException("Email đã được sử dụng");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RoleNotFoundException("Role is not existed"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Set.of(role));

        userRepository.save(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        try {
            authenticationManager.authenticate(authenticationToken);

            // Đưa Authentication vào SecurityContext và gán SecurityContext vào
            // SecurityContextHolder
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (BadCredentialsException e) {
            log.error("Error in login api: {}", e.getMessage());
            throw new BadCredentialsException("Invalid username or password");
        }

        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Generate JWT for client
        String accessToken = jwtService.generateAccessToken(user);

        return AuthResponse.builder()
                .email(user.getUsername())
                .name(user.getName())
                .role(user.getRoles().iterator().next().getName())
                .token(accessToken)
                .build();
    }
}
