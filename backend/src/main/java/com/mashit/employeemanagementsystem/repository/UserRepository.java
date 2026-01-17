package com.mashit.employeemanagementsystem.repository;

import com.mashit.employeemanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.employee.employeeId = :employeeId")
    Optional<User> findByEmployeeId(@Param("employeeId") Long employeeId);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.enabled = true")
    List<User> findAllActiveUsers();
}