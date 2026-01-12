import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { EmployeeService } from '../../shared/services/employee';
import { LeaveRequestService } from '../../shared/services/leave-request';
import { LoginResponse } from '../../shared/models/auth.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class DashboardComponent implements OnInit {
  currentUser: LoginResponse | null = null;
  stats = {
    totalEmployees: 0,
    pendingLeaveRequests: 0,
    activeUsers: 0,
    departments: 0
  };
  isLoading = true;
  greeting = '';

  constructor(
    private authService: AuthService,
    private employeeService: EmployeeService,
    private leaveRequestService: LeaveRequestService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser();
    this.setGreeting();
    this.loadStats();
  }

  setGreeting(): void {
    const hour = new Date().getHours();
    if (hour < 12) {
      this.greeting = 'Good Morning';
    } else if (hour < 18) {
      this.greeting = 'Good Afternoon';
    } else {
      this.greeting = 'Good Evening';
    }
  }

  loadStats(): void {
    this.isLoading = true;

    if (this.hasRole(['ADMIN', 'MANAGER'])) {
      this.employeeService.getAllEmployees().subscribe({
        next: (employees) => {
          this.stats.totalEmployees = employees.length;
          const departments = new Set(employees.map(e => e.department).filter(d => d));
          this.stats.departments = departments.size;
        },
        error: (error) => console.error('Error loading employees:', error)
      });

      this.leaveRequestService.getPendingLeaveRequests().subscribe({
        next: (requests) => {
          this.stats.pendingLeaveRequests = requests.length;
        },
        error: (error) => console.error('Error loading leave requests:', error)
      });
    }

    this.isLoading = false;
  }

  hasRole(roles: string[]): boolean {
    return this.authService.hasAnyRole(roles);
  }

  goToEmployees(): void {
    this.router.navigate(['/employees']);
  }

  goToLeaveRequests(): void {
    this.router.navigate(['/leave-requests']);
  }

  goToProfile(): void {
    this.router.navigate(['/profile']);
  }

  logout(): void {
    this.authService.logout();
  }
}