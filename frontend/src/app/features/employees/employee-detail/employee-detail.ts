import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { EmployeeService } from '../../../shared/services/employee';
import { AuthService } from '../../../core/services/auth.service';
import { Employee } from '../../../shared/models/employee.model';

@Component({
  selector: 'app-employee-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './employee-detail.html',
  styleUrl: './employee-detail.css'
})
export class EmployeeDetailComponent implements OnInit {
  employee: Employee | null = null;
  employeeId!: number;
  isLoading = true;
  errorMessage = '';

  constructor(
    private employeeService: EmployeeService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Get employee ID from route params
    this.route.params.subscribe(params => {
      this.employeeId = +params['id'];
      if (this.employeeId) {
        this.loadEmployee();
      }
    });
  }

  /**
   * Load employee details
   */
  loadEmployee(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.employeeService.getEmployeeById(this.employeeId).subscribe({
      next: (data) => {
        this.employee = data;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading employee:', error);
        this.errorMessage = 'Failed to load employee details.';
        this.isLoading = false;
        
        // Redirect after 3 seconds
        setTimeout(() => {
          this.router.navigate(['/employees']);
        }, 3000);
      }
    });
  }

  /**
   * Navigate to edit page
   */
  editEmployee(): void {
    this.router.navigate(['/employees/edit', this.employeeId]);
  }

  /**
   * Delete employee
   */
  deleteEmployee(): void {
    if (confirm(`Are you sure you want to delete ${this.employee?.firstName} ${this.employee?.lastName}?`)) {
      this.employeeService.deleteEmployee(this.employeeId).subscribe({
        next: () => {
          alert('Employee deleted successfully!');
          this.router.navigate(['/employees']);
        },
        error: (error) => {
          console.error('Error deleting employee:', error);
          alert('Failed to delete employee. Please try again.');
        }
      });
    }
  }

  /**
   * Check if user can manage employees
   */
  canManageEmployees(): boolean {
    return this.authService.hasAnyRole(['ADMIN', 'MANAGER']);
  }

  /**
   * Go back to list
   */
  goBack(): void {
    this.router.navigate(['/employees']);
  }

  /**
   * Logout
   */
  logout(): void {
    this.authService.logout();
  }
}