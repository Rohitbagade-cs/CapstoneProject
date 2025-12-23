package com.investmentbank.deal_pipeline.controller;

import com.investmentbank.deal_pipeline.dto.LoginRequest;
import com.investmentbank.deal_pipeline.entity.User;
import com.investmentbank.deal_pipeline.repository.UserRepository;
import com.investmentbank.deal_pipeline.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserRepository userRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        // ✅ Authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // ✅ Fetch user from DB
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ Extract role (ADMIN / ANALYST)
        String role = user.getRole().getName();

        // ✅ Generate JWT with ROLE
        return jwtUtil.generateToken(user.getUsername(), role);
    }
}
