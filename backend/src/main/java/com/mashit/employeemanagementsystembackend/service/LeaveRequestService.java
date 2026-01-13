package com.mashit.employeemanagementsystembackend.service;

import com.mashit.employeemanagementsystembackend.entity.Employee;
import com.mashit.employeemanagementsystembackend.entity.LeaveRequest;
import com.mashit.employeemanagementsystembackend.repository.EmployeeRepository;
import com.mashit.employeemanagementsystembackend.repository.LeaveRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository,
                               EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public Optional<LeaveRequest> getLeaveRequestById(Long id) {
        return leaveRequestRepository.findById(id);
    }

    public List<LeaveRequest> getLeaveRequestsByEmployee(Long employeeId) {
        return leaveRequestRepository.findByEmployee_EmployeeId(employeeId);
    }

    public List<LeaveRequest> getLeaveRequestsByStatus(String status) {
        return leaveRequestRepository.findByStatus(status);
    }

    public List<LeaveRequest> getPendingLeaveRequestsForEmployee(Long employeeId) {
        return leaveRequestRepository.findByEmployee_EmployeeIdAndStatus(employeeId, "PENDING");
    }

    public List<LeaveRequest> getLeaveRequestsByDateRange(LocalDate startDate, LocalDate endDate) {
        return leaveRequestRepository.findByStartDateBetween(startDate, endDate);
    }

    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        // Verify employee exists
        if (leaveRequest.getEmployee() != null &&
                leaveRequest.getEmployee().getEmployeeId() != null) {
            Employee employee = employeeRepository
                    .findById(leaveRequest.getEmployee().getEmployeeId())
                    .orElseThrow(() -> new RuntimeException(
                            "Employee not found with id: " + leaveRequest.getEmployee().getEmployeeId()
                    ));
            leaveRequest.setEmployee(employee);
        } else {
            throw new IllegalArgumentException("Employee is required");
        }

        // Validate dates
        if (leaveRequest.getStartDate() == null || leaveRequest.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }

        if (leaveRequest.getEndDate().isBefore(leaveRequest.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        // Set default status to PENDING if not provided
        if (leaveRequest.getStatus() == null || leaveRequest.getStatus().isEmpty()) {
            leaveRequest.setStatus("PENDING");
        }

        // Validate status
        if (!isValidStatus(leaveRequest.getStatus())) {
            throw new IllegalArgumentException(
                    "Invalid status. Must be PENDING, APPROVED, or REJECTED"
            );
        }

        return leaveRequestRepository.save(leaveRequest);
    }

    public LeaveRequest updateLeaveRequest(Long id, LeaveRequest leaveRequestDetails) {
        LeaveRequest existingRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));

        // Update dates if provided
        if (leaveRequestDetails.getStartDate() != null) {
            existingRequest.setStartDate(leaveRequestDetails.getStartDate());
        }

        if (leaveRequestDetails.getEndDate() != null) {
            existingRequest.setEndDate(leaveRequestDetails.getEndDate());
        }

        // Validate dates after update
        if (existingRequest.getEndDate().isBefore(existingRequest.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        // Update status if provided
        if (leaveRequestDetails.getStatus() != null) {
            if (!isValidStatus(leaveRequestDetails.getStatus())) {
                throw new IllegalArgumentException(
                        "Invalid status. Must be PENDING, APPROVED, or REJECTED"
                );
            }
            existingRequest.setStatus(leaveRequestDetails.getStatus());
        }

        return leaveRequestRepository.save(existingRequest);
    }

    public LeaveRequest approveLeaveRequest(Long id) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));

        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalArgumentException(
                    "Only pending requests can be approved"
            );
        }

        request.setStatus("APPROVED");
        return leaveRequestRepository.save(request);
    }

    public LeaveRequest rejectLeaveRequest(Long id) {
        LeaveRequest request = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));

        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalArgumentException(
                    "Only pending requests can be rejected"
            );
        }

        request.setStatus("REJECTED");
        return leaveRequestRepository.save(request);
    }

    public void deleteLeaveRequest(Long id) {
        if (!leaveRequestRepository.existsById(id)) {
            throw new RuntimeException("Leave request not found with id: " + id);
        }
        leaveRequestRepository.deleteById(id);
    }

    private boolean isValidStatus(String status) {
        return "PENDING".equals(status) ||
                "APPROVED".equals(status) ||
                "REJECTED".equals(status);
    }

    public long getTotalLeaveRequests() {
        return leaveRequestRepository.count();
    }

    public long getPendingLeaveRequestsCount() {
        return leaveRequestRepository.findByStatus("PENDING").size();
    }
}