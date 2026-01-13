package com.mashit.employeemanagementsystembackend.controller;

import com.mashit.employeemanagementsystembackend.entity.Users;
import com.mashit.employeemanagementsystembackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST CONTROLLER for User endpoints
 * Base URL: /api/users
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/users - Get all users
     */
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * GET /api/users/{id} - Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Users user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Users> getUserByUsername(@PathVariable String username) {
        Users user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Users> getUserByEmployeeId(@PathVariable Long employeeId) {
        Users user = userService.getUserByEmployeeId(employeeId);
        return ResponseEntity.ok(user);
    }
    /**
     * GET /api/users/enabled - Get all enabled users
     */
    @GetMapping("/enabled")
    public ResponseEntity<List<Users>> getEnabledUsers() {
        List<Users> users = userService.getEnabledUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * POST /api/users - Create new user
     */
    @PostMapping
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        try {
            Users createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/users/{id} - Update user
     */
    @PutMapping("/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable Long id,
                                            @RequestBody Users userDetails) {
        try {
            Users updatedUser = userService.updateUser(id, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/users/{id} - Delete user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /api/users/{id}/enable - Enable user account
     */
    @PutMapping("/{id}/enable")
    public ResponseEntity<Users> enableUser(@PathVariable Long id) {
        try {
            Users user = userService.enableUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /api/users/{id}/disable - Disable user account
     */
    @PutMapping("/{id}/disable")
    public ResponseEntity<Users> disableUser(@PathVariable Long id) {
        try {
            Users user = userService.disableUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}