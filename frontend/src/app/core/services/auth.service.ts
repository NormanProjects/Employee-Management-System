import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';

import { environment } from '../../../environments/environment';
import {
  LoginRequest,
  LoginResponse,
  RegisterRequest
} from '../../shared/models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly apiUrl = `${environment.apiUrl}/auth`;
  private readonly tokenKey = 'auth_token';
  private readonly userKey = 'current_user';

  private currentUserSubject = new BehaviorSubject<LoginResponse | null>(
    this.loadUserFromStorage()
  );

  currentUser$ = this.currentUserSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  /* ----------------------------
   * Auth API
   * ---------------------------- */

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiUrl}/login`, credentials)
      .pipe(tap(response => this.handleAuthSuccess(response)));
  }

  register(payload: RegisterRequest): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiUrl}/register`, payload)
      .pipe(tap(response => this.handleAuthSuccess(response)));
  }

  logout(): void {
    this.clearStorage();
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  /* ----------------------------
   * Password Reset
   * ---------------------------- */

  forgotPassword(email: string): Observable<any> { // Fixed typo: was 'orgotPassword'
    return this.http.post(`${this.apiUrl}/forgot-password`, { email });
  }

  validateResetToken(token: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/validate-reset-token?token=${token}`);
  }

  resetPassword(data: { token: string; newPassword: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset-password`, data);
  }

  /* ----------------------------
   * Authentication State
   * ---------------------------- */

  isAuthenticated(): boolean {
    const token = this.getToken();
    const user = this.currentUserSubject.value;

    if (!token || !user) {
      return false;
    }

    return true;
  }

  /* ----------------------------
   * Role Helpers
   * ---------------------------- */

  hasRole(role: string): boolean {
    return this.currentUserSubject.value?.roleName === role;
  }

  hasAnyRole(roles: string[]): boolean {
    const role = this.currentUserSubject.value?.roleName;
    return role ? roles.includes(role) : false;
  }

  /* ----------------------------
   * User Helpers
   * ---------------------------- */

  getCurrentUser(): LoginResponse | null {
    return this.currentUserSubject.value;
  }

  getUserRole(): string | null {
    return this.currentUserSubject.value?.roleName ?? null;
  }

  getUserId(): number | null {
    return this.currentUserSubject.value?.userId ?? null;
  }

  getEmployeeId(): number | null {
    return this.currentUserSubject.value?.employeeId ?? null;
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  /* ----------------------------
   * Private Helpers
   * ---------------------------- */

  private handleAuthSuccess(response: LoginResponse): void {
    localStorage.setItem(this.tokenKey, response.token);
    localStorage.setItem(this.userKey, JSON.stringify(response));
    this.currentUserSubject.next(response);
  }

  private clearStorage(): void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.userKey);
  }

  private loadUserFromStorage(): LoginResponse | null {
    const stored = localStorage.getItem(this.userKey);
    if (!stored) return null;

    try {
      return JSON.parse(stored);
    } catch {
      return null;
    }
  }

  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const now = Math.floor(Date.now() / 1000);
      return now >= payload.exp;
    } catch {
      return true;
    }
  }
}