import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { LeaveRequestService } from '../../../shared/services/leave-request';
import { AuthService } from '../../../core/services/auth.service';
import { LeaveRequest, LeaveStatus } from '../../../shared/models/leave-request.model';

@Component({
  selector: 'app-leave-request-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './leave-request-list.html',
  styleUrl: './leave-request-list.css'
})
export class LeaveRequestListComponent implements OnInit {
  leaveRequests: LeaveRequest[] = [];
  filteredRequests: LeaveRequest[] = [];
  isLoading = true;
  errorMessage = '';
  selectedStatus = '';
  LeaveStatus = LeaveStatus; // Expose enum to template

  constructor(
    private leaveRequestService: LeaveRequestService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadLeaveRequests();
  }

  /**
   * Load leave requests based on user role
   */
  loadLeaveRequests(): void {
    this.isLoading = true;
    this.errorMessage = '';

    // If ADMIN/MANAGER, show all requests. If EMPLOYEE, show only their requests
    if (this.isManagerOrAdmin()) {
      this.leaveRequestService.getAllLeaveRequests().subscribe({
        next: (data) => {
          this.leaveRequests = data;
          this.filteredRequests = data;
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error loading leave requests:', error);
          this.errorMessage = 'Failed to load leave requests.';
          this.isLoading = false;
        }
      });
    } else {
      // Load only employee's own requests
      const employeeId = this.authService.getEmployeeId();
      if (employeeId) {
        this.leaveRequestService.getLeaveRequestsByEmployee(employeeId).subscribe({
          next: (data) => {
            this.leaveRequests = data;
            this.filteredRequests = data;
            this.isLoading = false;
          },
          error: (error) => {
            console.error('Error loading leave requests:', error);
            this.errorMessage = 'Failed to load leave requests.';
            this.isLoading = false;
          }
        });
      }
    }
  }

  /**
   * Filter by status
   */
  filterByStatus(): void {
    if (!this.selectedStatus) {
      this.filteredRequests = this.leaveRequests;
    } else {
      this.filteredRequests = this.leaveRequests.filter(
        req => req.status === this.selectedStatus
      );
    }
  }

  /**
   * Reset filters
   */
  resetFilters(): void {
    this.selectedStatus = '';
    this.filteredRequests = this.leaveRequests;
  }

  /**
   * Navigate to create leave request
   */
  createLeaveRequest(): void {
    this.router.navigate(['/leave-requests/create']);
  }

  /**
   * Approve leave request
   */
  approveRequest(id: number): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    if (confirm('Are you sure you want to approve this leave request?')) {
      this.leaveRequestService.approveLeaveRequest(id, { approvedBy: userId }).subscribe({
        next: () => {
          alert('Leave request approved!');
          this.loadLeaveRequests();
        },
        error: (error) => {
          console.error('Error approving request:', error);
          alert('Failed to approve request.');
        }
      });
    }
  }

  /**
   * Reject leave request
   */
  rejectRequest(id: number): void {
    const reason = prompt('Please enter rejection reason:');
    if (reason) {
      this.leaveRequestService.rejectLeaveRequest(id, { rejectionReason: reason }).subscribe({
        next: () => {
          alert('Leave request rejected!');
          this.loadLeaveRequests();
        },
        error: (error) => {
          console.error('Error rejecting request:', error);
          alert('Failed to reject request.');
        }
      });
    }
  }

  /**
   * Cancel leave request
   */
  cancelRequest(id: number): void {
    if (confirm('Are you sure you want to cancel this leave request?')) {
      this.leaveRequestService.cancelLeaveRequest(id).subscribe({
        next: () => {
          alert('Leave request cancelled!');
          this.loadLeaveRequests();
        },
        error: (error) => {
          console.error('Error cancelling request:', error);
          alert('Failed to cancel request.');
        }
      });
    }
  }

  /**
   * Check if user is ADMIN or MANAGER
   */
  isManagerOrAdmin(): boolean {
    return this.authService.hasAnyRole(['ADMIN', 'MANAGER']);
  }

  /**
   * Check if can approve/reject
   */
  canApprove(request: LeaveRequest): boolean {
    return this.isManagerOrAdmin() && request.status === LeaveStatus.PENDING;
  }

  /**
   * Check if can cancel
   */
  canCancel(request: LeaveRequest): boolean {
    const employeeId = this.authService.getEmployeeId();
    return request.employeeId === employeeId && request.status === LeaveStatus.PENDING;
  }

  /**
   * Get status badge class
   */
  getStatusClass(status: string): string {
    switch (status) {
      case LeaveStatus.APPROVED: return 'status-approved';
      case LeaveStatus.REJECTED: return 'status-rejected';
      case LeaveStatus.CANCELLED: return 'status-cancelled';
      default: return 'status-pending';
    }
  }

  /**
   * Go back to dashboard
   */
  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  /**
   * Logout
   */
  logout(): void {
    this.authService.logout();
  }
}