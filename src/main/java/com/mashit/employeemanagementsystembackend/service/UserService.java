package com.mashit.employeemanagementsystembackend.service;

import com.mashit.employeemanagementsystembackend.entity.Employee;
import com.mashit.employeemanagementsystembackend.entity.Users;
import com.mashit.employeemanagementsystembackend.repository.EmployeeRepository;
import com.mashit.employeemanagementsystembackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public UserService(UserRepository userRepository,
                       EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<Users> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<Users> getUserByEmployeeId(Long employeeId) {
        return userRepository.findByEmployee_EmployeeId(employeeId);
    }

    public List<Users> getEnabledUsers() {
        return userRepository.findByEnabledTrue();
    }

    public Users createUser(Users user) {
        // Validate username uniqueness
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(
                    "Username '" + user.getUsername() + "' already exists"
            );
        }

        // Verify employee exists
        if (user.getEmployee() != null && user.getEmployee().getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(user.getEmployee().getEmployeeId())
                    .orElseThrow(() -> new RuntimeException(
                            "Employee not found with id: " + user.getEmployee().getEmployeeId()
                    ));

            // Check if employee already has a user account
            if (userRepository.findByEmployee_EmployeeId(employee.getEmployeeId()).isPresent()) {
                throw new IllegalArgumentException(
                        "Employee already has a user account"
                );
            }

            user.setEmployee(employee);
        } else {
            throw new IllegalArgumentException("Employee is required");
        }

        // TODO: In production, password should be encrypted with BCrypt
        // For now, we're storing plain text (NOT SECURE - will fix with Spring Security)
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default enabled to true if not specified
        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }

        return userRepository.save(user);
    }

    public Users updateUser(Long id, Users userDetails) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Check username uniqueness if changed
        if (!existingUser.getUsername().equals(userDetails.getUsername())) {
            if (userRepository.existsByUsername(userDetails.getUsername())) {
                throw new IllegalArgumentException("Username already in use");
            }
        }

        // Update fields
        existingUser.setUsername(userDetails.getUsername());

        // Only update password if provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            // TODO: Encrypt password with BCrypt
            existingUser.setPassword(userDetails.getPassword());
        }

        if (userDetails.getEnabled() != null) {
            existingUser.setEnabled(userDetails.getEnabled());
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public Users enableUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public Users disableUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setEnabled(false);
        return userRepository.save(user);
    }

    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
}