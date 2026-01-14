import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { EmployeeService } from '../../../shared/services/employee';
import { AuthService } from '../../../core/services/auth.service';
import { Employee } from '../../../shared/models/employee.model';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './employee-list.component.html',
  styleUrl: './employee-list.component.css'
})
export class EmployeeListComponent implements OnInit {
  employees: Employee[] = [];
  filteredEmployees: Employee[] = [];
  isLoading = true;
  errorMessage = '';
  searchTerm = '';
  selectedDepartment = '';
  departments: string[] = [];

  constructor(
    private employeeService: EmployeeService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadEmployees();
  }

  /**
   * Load all employees
   */
  loadEmployees(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        this.employees = data;
        this.filteredEmployees = data;
        this.extractDepartments();
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading employees:', error);
        this.errorMessage = 'Failed to load employees. Please try again.';
        this.isLoading = false;
      }
    });
  }

  /**
   * Extract unique departments from employees
   */
  extractDepartments(): void {
  const depts = this.employees
    .map(e => e.department)
    .filter(d => d) as string[];
  this.departments = [...new Set(depts)];
}
  /**
   * Filter employees based on search and department
   */
  filterEmployees(): void {
    this.filteredEmployees = this.employees.filter(emp => {
      const matchesSearch = 
        emp.firstName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        emp.lastName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        emp.email.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        (emp.position && emp.position.toLowerCase().includes(this.searchTerm.toLowerCase()));

      const matchesDepartment = 
        !this.selectedDepartment || emp.department === this.selectedDepartment;

      return matchesSearch && matchesDepartment;
    });
  }

  /**
   * Reset filters
   */
  resetFilters(): void {
    this.searchTerm = '';
    this.selectedDepartment = '';
    this.filteredEmployees = this.employees;
  }

  /**
   * Navigate to add employee page
   */
  addEmployee(): void {
    this.router.navigate(['/employees/create']);
  }

  /**
   * Navigate to edit employee page
   */
  editEmployee(id: number): void {
    this.router.navigate(['/employees/edit', id]);
  }

  /**
   * View employee details
   */
  viewEmployee(id: number): void {
    this.router.navigate(['/employees', id]);
  }

  /**
   * Delete employee
   */
  deleteEmployee(id: number, name: string): void {
    if (confirm(`Are you sure you want to delete ${name}?`)) {
      this.employeeService.deleteEmployee(id).subscribe({
        next: () => {
          // Remove from list
          this.employees = this.employees.filter(e => e.employeeId !== id);
          this.filterEmployees();
          alert('Employee deleted successfully!');
        },
        error: (error) => {
          console.error('Error deleting employee:', error);
          alert('Failed to delete employee. Please try again.');
        }
      });
    }
  }

  /**
   * Check if user has permission to edit/delete
   */
  canManageEmployees(): boolean {
    return this.authService.hasAnyRole(['ADMIN', 'MANAGER']);
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