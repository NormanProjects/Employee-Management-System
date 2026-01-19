import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UserService } from '../../../shared/services/user';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent implements OnInit {
  users: any[] = [];
  loading = true;
  error = '';

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) {
    console.log('UserListComponent initialized');
  }

  ngOnInit() {
    console.log('Loading users...');
    this.loadUsers();
  }

  loadUsers() {
    this.loading = true;
    this.error = '';

    this.userService.getAllUsers().subscribe({
      next: (users) => {
        console.log('Users loaded:', users);
        this.users = users;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading users:', error);
        this.error = 'Failed to load users: ' + (error.message || 'Unknown error');
        this.loading = false;
      }
    });
  }

  toggleUserStatus(userId: number, currentStatus: boolean) {
    const action = currentStatus ? 'deactivate' : 'activate';
    if (!confirm(`Are you sure you want to ${action} this user?`)) {
      return;
    }

    const request = currentStatus 
      ? this.userService.deactivateUser(userId)
      : this.userService.activateUser(userId);

    request.subscribe({
      next: () => {
        alert(`User ${action}d successfully!`);
        this.loadUsers();
      },
      error: (error) => {
        alert(`Failed to ${action} user: ` + error.message);
      }
    });
  }

  deleteUser(userId: number) {
    if (!confirm('Are you sure you want to delete this user?')) {
      return;
    }

    this.userService.deleteUser(userId).subscribe({
      next: () => {
        alert('User deleted successfully!');
        this.loadUsers();
      },
      error: (error) => {
        alert('Failed to delete user: ' + error.message);
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/dashboard']);
  }

  isAdmin(): boolean {
    return this.authService.hasRole('ADMIN');
  }
}