import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { EmployeeService } from '../../../shared/services/employee';
import { RoleService } from '../../../shared/services/role';
import { Role } from '../../../shared/models/role.model';
import { UpdateEmployeeRequest } from '../../../shared/models/employee.model';

@Component({
  selector: 'app-employee-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './employee-edit.html',
  styleUrl: './employee-edit.css'
})
export class EmployeeEditComponent implements OnInit {
  employeeForm: FormGroup;
  roles: Role[] = [];
  employeeId!: number;
  isLoading = false;
  isLoadingData = true;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private roleService: RoleService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // Initialize form with validators
    this.employeeForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.minLength(2)]],
      lastName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: [''],
      department: [''],
      position: [''],
      hireDate: [''],
      salary: ['', [Validators.min(0)]],
      roleId: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    // Get employee ID from route params
    this.route.params.subscribe(params => {
      this.employeeId = +params['id'];
      if (this.employeeId) {
        this.loadRoles();
        this.loadEmployee();
      }
    });
  }

  /**
   * Load all roles for dropdown
   */
  loadRoles(): void {
    this.roleService.getAllRoles().subscribe({
      next: (data) => {
        this.roles = data;
      },
      error: (error) => {
        console.error('Error loading roles:', error);
        this.errorMessage = 'Failed to load roles. Please refresh the page.';
      }
    });
  }

  /**
   * Load employee data
   */
  loadEmployee(): void {
    this.isLoadingData = true;
    
    this.employeeService.getEmployeeById(this.employeeId).subscribe({
      next: (employee) => {
        // Populate form with employee data
        this.employeeForm.patchValue({
          firstName: employee.firstName,
          lastName: employee.lastName,
          email: employee.email,
          phoneNumber: employee.phoneNumber || '',
          department: employee.department || '',
          position: employee.position || '',
          hireDate: employee.hireDate || '',
          salary: employee.salary || '',
          roleId: employee.roleId
        });
        this.isLoadingData = false;
      },
      error: (error) => {
        console.error('Error loading employee:', error);
        this.errorMessage = 'Failed to load employee data.';
        this.isLoadingData = false;
        
        // Redirect to list after 3 seconds
        setTimeout(() => {
          this.router.navigate(['/employees']);
        }, 3000);
      }
    });
  }

  /**
   * Handle form submission
   */
  onSubmit(): void {
    // Clear messages
    this.errorMessage = '';
    this.successMessage = '';

    // Validate form
    if (this.employeeForm.invalid) {
      this.markFormGroupTouched(this.employeeForm);
      return;
    }

    this.isLoading = true;

    // Prepare request (only send changed fields)
    const request: UpdateEmployeeRequest = {
      ...this.employeeForm.value,
      roleId: Number(this.employeeForm.value.roleId)
    };

    // Call service
    this.employeeService.updateEmployee(this.employeeId, request).subscribe({
      next: (response) => {
        this.successMessage = 'Employee updated successfully!';
        this.isLoading = false;
        
        // Redirect to employee list after 1.5 seconds
        setTimeout(() => {
          this.router.navigate(['/employees']);
        }, 1500);
      },
      error: (error) => {
        console.error('Error updating employee:', error);
        this.isLoading = false;
        
        if (error.status === 409) {
          this.errorMessage = 'An employee with this email already exists.';
        } else if (error.status === 400) {
          this.errorMessage = 'Invalid data. Please check all fields.';
        } else if (error.status === 404) {
          this.errorMessage = 'Employee not found.';
        } else {
          this.errorMessage = 'Failed to update employee. Please try again.';
        }
      }
    });
  }

  /**
   * Mark all form fields as touched to show validation errors
   */
  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  /**
   * Check if field has error
   */
  hasError(fieldName: string, errorType: string): boolean {
    const field = this.employeeForm.get(fieldName);
    return !!(field?.hasError(errorType) && field?.touched);
  }

  /**
   * Get error message for field
   */
  getErrorMessage(fieldName: string): string {
    const field = this.employeeForm.get(fieldName);
    
    if (field?.hasError('required')) {
      return `${this.capitalize(fieldName)} is required`;
    }
    
    if (field?.hasError('minlength')) {
      const minLength = field.errors?.['minlength'].requiredLength;
      return `${this.capitalize(fieldName)} must be at least ${minLength} characters`;
    }
    
    if (field?.hasError('email')) {
      return 'Please enter a valid email address';
    }
    
    if (field?.hasError('min')) {
      return 'Value must be greater than or equal to 0';
    }
    
    return '';
  }

  /**
   * Capitalize first letter
   */
  private capitalize(str: string): string {
    // Convert camelCase to separate words
    const words = str.replace(/([A-Z])/g, ' $1').toLowerCase();
    return words.charAt(0).toUpperCase() + words.slice(1);
  }

  /**
   * Cancel and go back
   */
  cancel(): void {
    if (confirm('Are you sure you want to cancel? All unsaved changes will be lost.')) {
      this.router.navigate(['/employees']);
    }
  }
}