import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router'; // Add this import
import { AuthService } from '../../core/services/auth.service';
import { UserService } from '../../shared/services/user';
import { EmployeeService } from '../../shared/services/employee';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  currentUser: any;
  employee: any;
  passwordForm: FormGroup;
  loading = true;
  error = '';
  successMessage = '';
  changingPassword = false;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private employeeService: EmployeeService,
    private fb: FormBuilder,
    private router: Router // Add this injection
  ) {
    this.currentUser = this.authService.getCurrentUser();
    
    this.passwordForm = this.fb.group({
      currentPassword: ['', [Validators.required]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]]
    }, {
      validators: this.passwordMatchValidator
    });
  }

  ngOnInit() {
    this.loadEmployeeDetails();
  }

  loadEmployeeDetails() {
    const employeeId = this.currentUser?.employeeId;
    if (employeeId) {
      this.employeeService.getEmployeeById(employeeId).subscribe({
        next: (employee) => {
          this.employee = employee;
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to load profile details';
          this.loading = false;
          console.error(error);
        }
      });
    }
  }

  passwordMatchValidator(formGroup: FormGroup) {
    const newPassword = formGroup.get('newPassword')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;
    
    if (newPassword !== confirmPassword) {
      formGroup.get('confirmPassword')?.setErrors({ passwordMismatch: true });
      return { passwordMismatch: true };
    }
    
    return null;
  }

  changePassword() {
    if (this.passwordForm.invalid) {
      return;
    }

    this.changingPassword = true;
    this.error = '';
    this.successMessage = '';

    const userId = this.currentUser?.userId;

    const passwordData = {
      currentPassword: this.passwordForm.value.currentPassword,
      newPassword: this.passwordForm.value.newPassword
    };

    this.userService.changePassword(userId, passwordData).subscribe({
      next: () => {
        this.successMessage = 'Password changed successfully!';
        this.passwordForm.reset();
        this.changingPassword = false;
      },
      error: (error) => {
        this.error =
          'Failed to change password: ' +
          (error.message || 'Invalid current password');
        this.changingPassword = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  logout() {
    this.authService.logout();
  }
}