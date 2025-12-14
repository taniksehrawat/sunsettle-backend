package com.sunsettle.service;

import com.sunsettle.dto.LoginRequest;
import com.sunsettle.dto.LoginResponse;
import com.sunsettle.dto.RegisterRequest;
import com.sunsettle.entity.User;
import com.sunsettle.entity.UserRole;
import com.sunsettle.repository.UserRepository;
import com.sunsettle.config.JwtUtil;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // REGISTER USER
    public String register(RegisterRequest req) {

        if (userRepository.findByEmail(req.getEmail()) != null) {
            return "User already exists!";
        }

        User user = User.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(UserRole.valueOf(req.getRole().toUpperCase()))
                .build();

        userRepository.save(user);
        return "User registered successfully!";
    }

    // LOGIN USER
    public LoginResponse login(LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail());

        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return LoginResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .name(user.getName())
                .build();
    }
}
