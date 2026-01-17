package com.mashit.employeemanagementsystem.service;

import com.mashit.employeemanagementsystem.entity.Role;
import com.mashit.employeemanagementsystem.exception.DuplicateResourceException;
import com.mashit.employeemanagementsystem.exception.ResourceNotFoundException;
import com.mashit.employeemanagementsystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + name));
    }

    @Transactional
    public Role createRole(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new DuplicateResourceException("Role with name " + role.getName() + " already exists");
        }
        return roleRepository.save(role);
    }

    @Transactional
    public Role updateRole(Long id, Role roleDetails) {
        Role role = getRoleById(id);

        if (!role.getName().equals(roleDetails.getName()) &&
                roleRepository.existsByName(roleDetails.getName())) {
            throw new DuplicateResourceException("Role with name " + roleDetails.getName() + " already exists");
        }

        role.setName(roleDetails.getName());
        return roleRepository.save(role);
    }

    @Transactional
    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }
}