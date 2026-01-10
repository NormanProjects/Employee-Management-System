package com.mashit.employeemanagementsystembackend.repository;

import com.mashit.employeemanagementsystembackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<Users> findByEmployee_EmployeeId(Long employeeId);
    List<Users> findByEnabledTrue();
}