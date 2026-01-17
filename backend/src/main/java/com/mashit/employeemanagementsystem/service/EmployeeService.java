package com.mashit.employeemanagementsystem.service;

import com.mashit.employeemanagementsystem.entity.Employee;
import com.mashit.employeemanagementsystem.entity.Role;
import com.mashit.employeemanagementsystem.exception.DuplicateResourceException;
import com.mashit.employeemanagementsystem.exception.ResourceNotFoundException;
import com.mashit.employeemanagementsystem.repository.EmployeeRepository;
import com.mashit.employeemanagementsystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with email: " + email));
    }

    public List<Employee> searchEmployeesByName(String name) {
        return employeeRepository.searchByName(name);
    }

    @Transactional
    public Employee createEmployee(Employee employee) {
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new DuplicateResourceException("Employee with email " + employee.getEmail() + " already exists");
        }

        if (employee.getRole() != null && employee.getRole().getRoleId() != null) {
            Role role = roleRepository.findById(employee.getRole().getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            employee.setRole(role);
        }

        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = getEmployeeById(id);

        if (!employee.getEmail().equals(employeeDetails.getEmail()) &&
                employeeRepository.existsByEmail(employeeDetails.getEmail())) {
            throw new DuplicateResourceException("Employee with email " + employeeDetails.getEmail() + " already exists");
        }

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmail(employeeDetails.getEmail());

        if (employeeDetails.getRole() != null && employeeDetails.getRole().getRoleId() != null) {
            Role role = roleRepository.findById(employeeDetails.getRole().getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            employee.setRole(role);
        }

        return employeeRepository.save(employee);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        Employee employee = getEmployeeById(id);
        employeeRepository.delete(employee);
    }
}