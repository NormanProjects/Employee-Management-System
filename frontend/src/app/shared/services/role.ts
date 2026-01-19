import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Role, CreateRoleRequest, UpdateRoleRequest } from '../models/role.model';

@Injectable({
  providedIn: 'root'
})
export class RoleService {
  private apiUrl = `${environment.apiUrl}/roles`;

  constructor(private http: HttpClient) {}

/*
  Get all roles
 */
getAllRoles(): Observable<Role[]> {
  return this.http.get<Role[]>(this.apiUrl);
}

  /**
   * Get role by ID
   */
  getRoleById(id: number): Observable<Role> {
    return this.http.get<Role>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create new role
   */
  createRole(role: CreateRoleRequest): Observable<Role> {
    return this.http.post<Role>(this.apiUrl, role);
  }

  /**
   * Update existing role
   */
  updateRole(id: number, role: UpdateRoleRequest): Observable<Role> {
    return this.http.put<Role>(`${this.apiUrl}/${id}`, role);
  }

  /**
   * Delete role
   */
  deleteRole(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Get role by name
   */
  getRoleByName(roleName: string): Observable<Role> {
    return this.http.get<Role>(`${this.apiUrl}/name/${roleName}`);
  }
}