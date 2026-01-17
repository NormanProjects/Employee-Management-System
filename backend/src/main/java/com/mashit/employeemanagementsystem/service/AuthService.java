package com.mashit.employeemanagementsystem.service;

import com.mashit.employeemanagementsystem.dto.JwtResponse;
import com.mashit.employeemanagementsystem.dto.LoginRequest;
import com.mashit.employeemanagementsystem.dto.RegisterRequest;
import com.mashit.employeemanagementsystem.entity.Employee;
import com.mashit.employeemanagementsystem.entity.User;
import com.mashit.employeemanagementsystem.exception.DuplicateResourceException;
import com.mashit.employeemanagementsystem.exception.ResourceNotFoundException;
import com.mashit.employeemanagementsystem.repository.EmployeeRepository;
import com.mashit.employeemanagementsystem.repository.UserRepository;
import com.mashit.employeemanagementsystem.security.CustomUserDetails;
import com.mashit.employeemanagementsystem.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public JwtResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmployee(employee);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        CustomUserDetails userDetails = CustomUserDetails.build(savedUser);
        String token = jwtUtil.generateToken(userDetails);

        return new JwtResponse(
                token,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmployee().getEmployeeId(),
                savedUser.getEmployee().getRole().getName()
        );
    }

    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwtUtil.generateToken(userDetails);

        return new JwtResponse(
                token,
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getEmployeeId(),
                userDetails.getRoleName()
        );
    }
}