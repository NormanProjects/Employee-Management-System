package com.mashit.employeemanagementsystembackend.service;

import com.mashit.employeemanagementsystembackend.entity.Employee;
import com.mashit.employeemanagementsystembackend.entity.Roles;
import com.mashit.employeemanagementsystembackend.exception.DuplicateResourceException;
import com.mashit.employeemanagementsystembackend.exception.ResourceNotFoundException;
import com.mashit.employeemanagementsystembackend.repository.EmployeeRepository;
import com.mashit.employeemanagementsystembackend.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    public EmployeeService(EmployeeRepository employeeRepository,
                           RoleRepository roleRepository) {
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    }

    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "email", email));
    }

    public List<Employee> getEmployeesByRole(Long roleId) {
        return employeeRepository.findByRole_RoleId(roleId);
    }

    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    public Employee createEmployee(Employee employee) {
        // Check if email already exists
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new DuplicateResourceException("Employee", "email", employee.getEmail());
        }

        // Verify role exists
        if (employee.getRole() != null && employee.getRole().getRoleId() != null) {
            Roles role = roleRepository.findById(employee.getRole().getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Role", "id", employee.getRole().getRoleId()));
            employee.setRole(role);
        } else {
            throw new IllegalArgumentException("Role is required");
        }

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));

        // Check email uniqueness if changed
        if (!existingEmployee.getEmail().equals(employeeDetails.getEmail())) {
            if (employeeRepository.existsByEmail(employeeDetails.getEmail())) {
                throw new DuplicateResourceException("Employee", "email", employeeDetails.getEmail());
            }
        }

        // Update fields
        existingEmployee.setFirstName(employeeDetails.getFirstName());
        existingEmployee.setLastName(employeeDetails.getLastName());
        existingEmployee.setEmail(employeeDetails.getEmail());

        // Update role if provided
        if (employeeDetails.getRole() != null && employeeDetails.getRole().getRoleId() != null) {
            Roles role = roleRepository.findById(employeeDetails.getRole().getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Role", "id", employeeDetails.getRole().getRoleId()));
            existingEmployee.setRole(role);
        }

        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee", "id", id);
        }
        employeeRepository.deleteById(id);
    }

    public boolean employeeExists(Long id) {
        return employeeRepository.existsById(id);
    }

    public long getTotalEmployeeCount() {
        return employeeRepository.count();
    }
}