import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { EmployeeService } from '../../../shared/services/employee';
import { RoleService } from '../../../shared/services/role';
import { Role } from '../../../shared/models/role.model';
import { CreateEmployeeRequest } from '../../../shared/models/employee.model';

@Component({
  selector: 'app-employee-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './employee-create.html',
  styleUrl: './employee-create.css'
})
export class EmployeeCreateComponent implements OnInit {
  employeeForm: FormGroup;
  roles: Role[] = [];
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private roleService: RoleService,
    private router: Router
  ) {
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
    this.loadRoles();
  }

  loadRoles(): void {
    console.log('Loading roles...');
    this.roleService.getAllRoles().subscribe({
      next: (data) => {
        console.log('Roles loaded:', data);
        this.roles = data;
      },
      error: (error) => {
        console.error('Error loading roles:', error);
        this.errorMessage = 'Failed to load roles. Please refresh the page.';
      }
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (this.employeeForm.invalid) {
      this.markFormGroupTouched(this.employeeForm);
      console.log('Form is invalid');
      return;
    }

    this.isLoading = true;

    const formValue = this.employeeForm.value;
    
    // Log the raw form values
    console.log('Raw form values:', formValue);
    console.log('RoleId value:', formValue.roleId);
    console.log('RoleId type:', typeof formValue.roleId);

    // Manually construct the request to match backend expectations
    const request: any = {
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      email: formValue.email,
      role: {
        roleId: Number(formValue.roleId)
      }
    };

    // Add optional fields only if they have values
    if (formValue.phoneNumber) {
      request.phoneNumber = formValue.phoneNumber;
    }
    if (formValue.department) {
      request.department = formValue.department;
    }
    if (formValue.position) {
      request.position = formValue.position;
    }
    if (formValue.hireDate) {
      request.hireDate = formValue.hireDate;
    }
    if (formValue.salary) {
      request.salary = Number(formValue.salary);
    }

    // Log the request being sent
    console.log('Request being sent:', request);
    console.log('Request roleId:', request.roleId);
    console.log('Request roleId type:', typeof request.roleId);
    console.log('JSON stringified:', JSON.stringify(request));

    this.employeeService.createEmployee(request).subscribe({
      next: (response) => {
        console.log('Success response:', response);
        this.successMessage = 'Employee created successfully!';
        this.isLoading = false;
        
        setTimeout(() => {
          this.router.navigate(['/employees']);
        }, 1500);
      },
      error: (error) => {
        console.error('Error creating employee:', error);
        console.error('Error details:', error.error);
        this.isLoading = false;
        
        if (error.status === 409) {
          this.errorMessage = 'An employee with this email already exists.';
        } else if (error.status === 400) {
          this.errorMessage = 'Invalid data. Please check all fields.';
        } else if (error.status === 500) {
          this.errorMessage = 'Server error. Please check all required fields are filled.';
        } else {
          this.errorMessage = 'Failed to create employee. Please try again.';
        }
      }
    });
  }

  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.keys(formGroup.controls).forEach(key => {
      const control = formGroup.get(key);
      control?.markAsTouched();
    });
  }

  hasError(fieldName: string, errorType: string): boolean {
    const field = this.employeeForm.get(fieldName);
    return !!(field?.hasError(errorType) && field?.touched);
  }

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

  private capitalize(str: string): string {
    const words = str.replace(/([A-Z])/g, ' $1').toLowerCase();
    return words.charAt(0).toUpperCase() + words.slice(1);
  }

  goBack(): void {
    this.router.navigate(['/employees']);
  }

  cancel(): void {
    if (confirm('Are you sure you want to cancel? All unsaved changes will be lost.')) {
      this.router.navigate(['/employees']);
    }
  }
}