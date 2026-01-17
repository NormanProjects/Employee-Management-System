package com.mashit.employeemanagementsystem.service;

import com.mashit.employeemanagementsystem.entity.Employee;
import com.mashit.employeemanagementsystem.entity.LeaveRequest;
import com.mashit.employeemanagementsystem.exception.ResourceNotFoundException;
import com.mashit.employeemanagementsystem.repository.EmployeeRepository;
import com.mashit.employeemanagementsystem.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;

    public List<LeaveRequest> getAllLeaveRequests() {
        return leaveRequestRepository.findAll();
    }

    public LeaveRequest getLeaveRequestById(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Leave request not found with id: " + id));
    }

    public List<LeaveRequest> getLeaveRequestsByEmployeeId(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId);
    }

    public List<LeaveRequest> getPendingLeaveRequests() {
        return leaveRequestRepository.findPendingLeaveRequests();
    }

    @Transactional
    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        leaveRequest.setEmployee(employee);
        leaveRequest.setStatus("PENDING");

        return leaveRequestRepository.save(leaveRequest);
    }

    @Transactional
    public LeaveRequest updateLeaveRequestStatus(Long id, String status) {
        LeaveRequest leaveRequest = getLeaveRequestById(id);
        leaveRequest.setStatus(status);
        return leaveRequestRepository.save(leaveRequest);
    }

    @Transactional
    public void deleteLeaveRequest(Long id) {
        LeaveRequest leaveRequest = getLeaveRequestById(id);
        leaveRequestRepository.delete(leaveRequest);
    }
}