import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User, CreateUserRequest, UpdateUserRequest, ChangePasswordRequest } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  /**
   * Get all users
   */
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  /**
   * Get user by ID
   */
  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create new user
   */
  createUser(user: CreateUserRequest): Observable<User> {
    return this.http.post<User>(this.apiUrl, user);
  }

  /**
   * Update existing user
   */
  updateUser(id: number, user: UpdateUserRequest): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, user);
  }

  /**
   * Delete user
   */
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Get user by username
   */
  getUserByUsername(username: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/username/${username}`);
  }

  /**
   * Get user by employee ID
   */
  getUserByEmployeeId(employeeId: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/employee/${employeeId}`);
  }

  /**
   * Change user password
   */
  changePassword(userId: number, request: ChangePasswordRequest): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${userId}/change-password`, request);
  }

  /**
   * Activate user
   */
  activateUser(userId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${userId}/activate`, {});
  }

  /**
   * Deactivate user
   */
  deactivateUser(userId: number): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${userId}/deactivate`, {});
  }
}