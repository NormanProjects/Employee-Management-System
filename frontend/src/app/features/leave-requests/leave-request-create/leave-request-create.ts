import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { LeaveRequestService } from '../../../shared/services/leave-request';
import { AuthService } from '../../../core/services/auth.service';
import { LeaveType, CreateLeaveRequestRequest } from '../../../shared/models/leave-request.model';

@Component({
  selector: 'app-leave-request-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './leave-request-create.html',
  styleUrl: './leave-request-create.css'
})
export class LeaveRequestCreateComponent implements OnInit {
  leaveForm: FormGroup;
  leaveTypes = Object.values(LeaveType);
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  employeeId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private leaveRequestService: LeaveRequestService,
    private authService: AuthService,
    private router: Router
  ) {
    this.leaveForm = this.fb.group({
      leaveType: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      reason: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  ngOnInit(): void {
    this.employeeId = this.authService.getEmployeeId();
    if (!this.employeeId) {
      this.errorMessage = 'Employee ID not found. Please contact administrator.';
    }
  }

  /**
   * Handle form submission
   */
  onSubmit(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (this.leaveForm.invalid) {
      this.markFormGroupTouched(this.leaveForm);
      return;
    }

    if (!this.employeeId) {
      this.errorMessage = 'Employee ID not found.';
      return;
    }

    // Validate date range
    const startDate = new Date(this.leaveForm.value.startDate);
    const endDate = new Date(this.leaveForm.value.endDate);
    
    if (endDate < startDate) {
      this.errorMessage = 'End date must be after start date.';
      return;
    }

    this.isLoading = true;

    const request: CreateLeaveRequestRequest = {
      employeeId: this.employeeId,
      leaveType: this.leaveForm.value.leaveType,
      startDate: this.leaveForm.value.startDate,
      endDate: this.leaveForm.value.endDate,
      reason: this.leaveForm.value.reason
    };

    this.leaveRequestService.createLeaveRequest(request).subscribe({
      next: (response) => {
        this.successMessage = 'Leave request submitted successfully!';
        this.isLoading = false;
        
        setTimeout(() => {
          this.router.navigate(['/leave-requests']);
        }, 1500);
      },
      error: (error) => {
        console.error('Error creating leave request:', error);
        this.isLoading = false;
        
        if (error.status === 400) {
          this.errorMessage = 'Invalid data. Please check all fields.';
        } else {
          this.errorMessage = 'Failed to submit leave request. Please try again.';
        }
      }
    });
  }

  /**
   * Mark all form fields as touched
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
    const field = this.leaveForm.get(fieldName);
    return !!(field?.hasError(errorType) && field?.touched);
  }

  /**
   * Get error message
   */
  getErrorMessage(fieldName: string): string {
    const field = this.leaveForm.get(fieldName);
    
    if (field?.hasError('required')) {
      return `${this.capitalize(fieldName)} is required`;
    }
    
    if (field?.hasError('minlength')) {
      const minLength = field.errors?.['minlength'].requiredLength;
      return `${this.capitalize(fieldName)} must be at least ${minLength} characters`;
    }
    
    return '';
  }

  /**
   * Capitalize first letter
   */
  private capitalize(str: string): string {
    const words = str.replace(/([A-Z])/g, ' $1').toLowerCase();
    return words.charAt(0).toUpperCase() + words.slice(1);
  }

  /**
   * Cancel and go back
   */
  cancel(): void {
    if (confirm('Are you sure you want to cancel?')) {
      this.router.navigate(['/leave-requests']);
    }
  }
}