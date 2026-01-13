package com.mashit.employeemanagementsystembackend.repository;

import com.mashit.employeemanagementsystembackend.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    List<LeaveRequest> findByEmployee_EmployeeId(Long employeeId);
    List<LeaveRequest> findByStatus(String status);
    List<LeaveRequest> findByEmployee_EmployeeIdAndStatus(Long employeeId, String status);
    List<LeaveRequest> findByStartDateBetween(LocalDate startDate, LocalDate endDate);
}
