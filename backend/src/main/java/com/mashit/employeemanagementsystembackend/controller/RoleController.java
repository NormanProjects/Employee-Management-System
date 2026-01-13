package com.mashit.employeemanagementsystembackend.controller;

import com.mashit.employeemanagementsystembackend.entity.Roles;
import com.mashit.employeemanagementsystembackend.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST CONTROLLER for Role endpoints
 * Base URL: /api/roles
 */
@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "*") // Allow requests from Angular frontend
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * GET /api/roles - Get all roles
     * @return List of all roles
     */
    @GetMapping
    public ResponseEntity<List<Roles>> getAllRoles() {
        List<Roles> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * GET /api/roles/{id} - Get role by ID
     * @param id - Role ID
     * @return Role if found, 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Roles> getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/roles/name/{name} - Get role by name
     * @param name - Role name (e.g., "ADMIN")
     * @return Role if found, 404 if not found
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Roles> getRoleByName(@PathVariable String name) {
        return roleService.getRoleByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/roles - Create new role
     * @param role - Role object from request body
     * @return Created role with 201 status
     */
    @PostMapping
    public ResponseEntity<Roles> createRole(@RequestBody Roles role) {
        try {
            Roles createdRole = roleService.createRole(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/roles/{id} - Update existing role
     * @param id - Role ID to update
     * @param roleDetails - New role details
     * @return Updated role
     */
    @PutMapping("/{id}")
    public ResponseEntity<Roles> updateRole(@PathVariable Long id,
                                            @RequestBody Roles roleDetails) {
        try {
            Roles updatedRole = roleService.updateRole(id, roleDetails);
            return ResponseEntity.ok(updatedRole);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/roles/{id} - Delete role
     * @param id - Role ID to delete
     * @return 204 No Content if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}