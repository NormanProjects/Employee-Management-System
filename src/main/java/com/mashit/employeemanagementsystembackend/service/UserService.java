package com.mashit.employeemanagementsystembackend.service;

import com.mashit.employeemanagementsystembackend.entity.Employee;
import com.mashit.employeemanagementsystembackend.entity.Users;
import com.mashit.employeemanagementsystembackend.exception.DuplicateResourceException;
import com.mashit.employeemanagementsystembackend.exception.ResourceNotFoundException;
import com.mashit.employeemanagementsystembackend.repository.EmployeeRepository;
import com.mashit.employeemanagementsystembackend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder; // Add password encoder

    public UserService(UserRepository userRepository,
                       EmployeeRepository employeeRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    public Users getUserByEmployeeId(Long employeeId) {
        return userRepository.findByEmployee_EmployeeId(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "employeeId", employeeId));
    }

    public List<Users> getEnabledUsers() {
        return userRepository.findByEnabledTrue();
    }

    public Users createUser(Users user) {
        // Validate username uniqueness
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("User", "username", user.getUsername());
        }

        // Verify employee exists
        if (user.getEmployee() != null && user.getEmployee().getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(user.getEmployee().getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Employee", "id", user.getEmployee().getEmployeeId()));

            // Check if employee already has a user account
            if (userRepository.findByEmployee_EmployeeId(employee.getEmployeeId()).isPresent()) {
                throw new IllegalArgumentException("Employee already has a user account");
            }

            user.setEmployee(employee);
        } else {
            throw new IllegalArgumentException("Employee is required");
        }

        // ENCRYPT PASSWORD with BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default enabled to true if not specified
        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }

        return userRepository.save(user);
    }

    public Users updateUser(Long id, Users userDetails) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Check username uniqueness if changed
        if (!existingUser.getUsername().equals(userDetails.getUsername())) {
            if (userRepository.existsByUsername(userDetails.getUsername())) {
                throw new DuplicateResourceException("User", "username", userDetails.getUsername());
            }
        }

        // Update fields
        existingUser.setUsername(userDetails.getUsername());

        // Only update password if provided (and encrypt it)
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        if (userDetails.getEnabled() != null) {
            existingUser.setEnabled(userDetails.getEnabled());
        }

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }

    public Users enableUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public Users disableUser(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setEnabled(false);
        return userRepository.save(user);
    }

    public boolean userExists(Long id) {
        return userRepository.existsById(id);
    }
}