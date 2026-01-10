package com.mashit.employeemanagementsystembackend.repository;

import com.mashit.employeemanagementsystembackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Employee> findByRole_RoleId(Long roleId);
    List<Employee> findByLastNameContainingIgnoreCase(String lastName);
}