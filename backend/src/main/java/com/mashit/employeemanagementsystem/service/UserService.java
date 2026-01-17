package com.mashit.employeemanagementsystem.service;

import com.mashit.employeemanagementsystem.entity.Employee;
import com.mashit.employeemanagementsystem.entity.User;
import com.mashit.employeemanagementsystem.exception.DuplicateResourceException;
import com.mashit.employeemanagementsystem.exception.ResourceNotFoundException;
import com.mashit.employeemanagementsystem.repository.EmployeeRepository;
import com.mashit.employeemanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllActiveUsers() {
        return userRepository.findAllActiveUsers();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getEmployee() != null && user.getEmployee().getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(user.getEmployee().getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
            user.setEmployee(employee);
        }

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        if (!user.getUsername().equals(userDetails.getUsername()) &&
                userRepository.existsByUsername(userDetails.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        user.setUsername(userDetails.getUsername());

        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        if (userDetails.getEmployee() != null && userDetails.getEmployee().getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(userDetails.getEmployee().getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
            user.setEmployee(employee);
        }

        return userRepository.save(user);
    }

    @Transactional
    public User toggleUserStatus(Long id) {
        User user = getUserById(id);
        user.setEnabled(!user.getEnabled());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}