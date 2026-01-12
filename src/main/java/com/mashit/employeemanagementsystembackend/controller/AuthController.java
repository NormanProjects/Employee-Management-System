package com.mashit.employeemanagementsystembackend.controller;

import com.mashit.employeemanagementsystembackend.dto.JwtResponse;
import com.mashit.employeemanagementsystembackend.dto.LoginRequest;
import com.mashit.employeemanagementsystembackend.entity.Users;
import com.mashit.employeemanagementsystembackend.repository.UserRepository;
import com.mashit.employeemanagementsystembackend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserDetailsService userDetailsService,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String jwt = jwtUtil.generateToken(userDetails);

        Users user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String employeeName = user.getEmployee() != null
                ? user.getEmployee().getFirstName() + " " + user.getEmployee().getLastName()
                : "Unknown";

        String role = user.getEmployee() != null && user.getEmployee().getRole() != null
                ? user.getEmployee().getRole().getName()
                : "EMPLOYEE";

        JwtResponse response = new JwtResponse(jwt, user.getId(), user.getUsername(), employeeName, role);
        return ResponseEntity.ok(response);
    }
}
