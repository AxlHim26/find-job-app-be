package com.example.Boilerplate_JWTBasedAuthentication.service;

import com.example.Boilerplate_JWTBasedAuthentication.dto.request.ChangePasswordRequest;
import com.example.Boilerplate_JWTBasedAuthentication.entity.User;
import com.example.Boilerplate_JWTBasedAuthentication.exception.custome.WrongCurrentPasswordException;
import com.example.Boilerplate_JWTBasedAuthentication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(String username, ChangePasswordRequest request) throws WrongCurrentPasswordException {
        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        // kiểm tra mật khẩu hiện tại có đúng với trong db hay không
        if (!passwordEncoder.matches(request.getCurrent(), user.getPassword())) {
            throw new WrongCurrentPasswordException("Current password is wrong");
        }

        // Cập nhật mật khẩu mới (mã hóa)
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
