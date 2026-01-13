package com.mashit.employeemanagementsystembackend.controller;

import com.mashit.employeemanagementsystembackend.entity.LeaveRequest;
import com.mashit.employeemanagementsystembackend.service.LeaveRequestService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST CONTROLLER for Leave Request endpoints
 * Base URL: /api/leave-requests
 */
@RestController
@RequestMapping("/api/leave-requests")
@CrossOrigin(origins = "*")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    /**
     * GET /api/leave-requests - Get all leave requests
     */
    @GetMapping
    public ResponseEntity<List<LeaveRequest>> getAllLeaveRequests() {
        List<LeaveRequest> requests = leaveRequestService.getAllLeaveRequests();
        return ResponseEntity.ok(requests);
    }

    /**
     * GET /api/leave-requests/{id} - Get leave request by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequest> getLeaveRequestById(@PathVariable Long id) {
        return leaveRequestService.getLeaveRequestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET /api/leave-requests/employee/{employeeId} - Get requests by employee
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByEmployee(@PathVariable Long employeeId) {
        List<LeaveRequest> requests = leaveRequestService.getLeaveRequestsByEmployee(employeeId);
        return ResponseEntity.ok(requests);
    }

    /**
     * GET /api/leave-requests/status/{status} - Get requests by status
     * Status: PENDING, APPROVED, REJECTED
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByStatus(@PathVariable String status) {
        List<LeaveRequest> requests = leaveRequestService.getLeaveRequestsByStatus(status);
        return ResponseEntity.ok(requests);
    }

    /**
     * GET /api/leave-requests/employee/{employeeId}/pending - Get pending requests for employee
     */
    @GetMapping("/employee/{employeeId}/pending")
    public ResponseEntity<List<LeaveRequest>> getPendingRequestsForEmployee(@PathVariable Long employeeId) {
        List<LeaveRequest> requests = leaveRequestService.getPendingLeaveRequestsForEmployee(employeeId);
        return ResponseEntity.ok(requests);
    }

    /**
     * GET /api/leave-requests/date-range?start={date}&end={date} - Get requests in date range
     * Date format: yyyy-MM-dd
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<LeaveRequest> requests = leaveRequestService.getLeaveRequestsByDateRange(start, end);
        return ResponseEntity.ok(requests);
    }

    /**
     * POST /api/leave-requests - Create new leave request
     */
    @PostMapping
    public ResponseEntity<LeaveRequest> createLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        try {
            LeaveRequest createdRequest = leaveRequestService.createLeaveRequest(leaveRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/leave-requests/{id} - Update leave request
     */
    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequest> updateLeaveRequest(@PathVariable Long id,
                                                           @RequestBody LeaveRequest leaveRequestDetails) {
        try {
            LeaveRequest updatedRequest = leaveRequestService.updateLeaveRequest(id, leaveRequestDetails);
            return ResponseEntity.ok(updatedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /api/leave-requests/{id}/approve - Approve leave request
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<LeaveRequest> approveLeaveRequest(@PathVariable Long id) {
        try {
            LeaveRequest approvedRequest = leaveRequestService.approveLeaveRequest(id);
            return ResponseEntity.ok(approvedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/leave-requests/{id}/reject - Reject leave request
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<LeaveRequest> rejectLeaveRequest(@PathVariable Long id) {
        try {
            LeaveRequest rejectedRequest = leaveRequestService.rejectLeaveRequest(id);
            return ResponseEntity.ok(rejectedRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * DELETE /api/leave-requests/{id} - Delete leave request
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        try {
            leaveRequestService.deleteLeaveRequest(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/leave-requests/count - Get total leave request count
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalLeaveRequestCount() {
        long count = leaveRequestService.getTotalLeaveRequests();
        return ResponseEntity.ok(count);
    }

    /**
     * GET /api/leave-requests/count/pending - Get pending request count
     */
    @GetMapping("/count/pending")
    public ResponseEntity<Long> getPendingLeaveRequestCount() {
        long count = leaveRequestService.getPendingLeaveRequestsCount();
        return ResponseEntity.ok(count);
    }
}