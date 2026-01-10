package com.mashit.employeemanagementsystembackend.service;

import com.mashit.employeemanagementsystembackend.entity.Roles;
import com.mashit.employeemanagementsystembackend.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service  // Marks this as a Spring service component
@Transactional  // Database transactions managed automatically
public class RoleService {

    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
      Get all roles
     @return List of all roles in database
     */
    public List<Roles> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Get role by ID
     * @param id - Role ID to search for
     * @return Optional<Roles> - may be empty if not found
     */
    public Optional<Roles> getRoleById(Long id) {
        return roleRepository.findById(id);
    }
    /**
     * Get role by name
     * @param name - Role name (e.g., "ADMIN", "MANAGER")
     * @return Optional<Roles> - may be empty if not found
     */
    public Optional<Roles> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    /**
     * Create new role
     * Business Rule: Role name must be unique
     *
     * @param role - Role to create
     * @return Created role with generated ID
     * @throws IllegalArgumentException if role name already exists
     */
    public Roles createRole(Roles role) {
        // Validation: Check if role name already exists
        if (roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException(
                    "Role with name '" + role.getName() + "' already exists"
            );
        }

        // Business rule: Role name should be uppercase
        role.setName(role.getName().toUpperCase());

        // Save and return
        return roleRepository.save(role);
    }

    /**
     * Update existing role
     *
     * @param id - ID of role to update
     * @param roleDetails - New role details
     * @return Updated role
     * @throws RuntimeException if role not found
     */
    public Roles updateRole(Long id, Roles roleDetails) {
        // Check if role exists
        Roles existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Update fields
        existingRole.setName(roleDetails.getName().toUpperCase());

        // Save and return
        return roleRepository.save(existingRole);
    }

    /**
     * Delete role by ID
     *
     * @param id - ID of role to delete
     * @throws RuntimeException if role not found
     */
    public void deleteRole(Long id) {
        // Check if role exists
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }

        roleRepository.deleteById(id);
    }

    /**
     * Check if role exists
     *
     * @param id - Role ID to check
     * @return true if exists, false otherwise
     */
    public boolean roleExists(Long id) {
        return roleRepository.existsById(id);
    }
}
