package com.mashit.employeemanagementsystem.repository;

import com.mashit.employeemanagementsystem.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.employeeId = :employeeId")
    List<LeaveRequest> findByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.status = :status")
    List<LeaveRequest> findByStatus(@Param("status") String status);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.status = 'PENDING'")
    List<LeaveRequest> findPendingLeaveRequests();
}