import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Employee, CreateEmployeeRequest, UpdateEmployeeRequest } from '../models/employee.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private apiUrl = `${environment.apiUrl}/employees`;

  constructor(private http: HttpClient) {}

  /**
   * Get all employees
   */
  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.apiUrl);
  }

  /**
   * Get employee by ID
   */
  getEmployeeById(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create new employee
   */
  createEmployee(employee: CreateEmployeeRequest): Observable<Employee> {
    return this.http.post<Employee>(this.apiUrl, employee);
  }

  /**
   * Update existing employee
   */
  updateEmployee(id: number, employee: UpdateEmployeeRequest): Observable<Employee> {
    return this.http.put<Employee>(`${this.apiUrl}/${id}`, employee);
  }

  /**
   * Delete employee
   */
  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Search employees by name
   */
  searchEmployeesByName(name: string): Observable<Employee[]> {
    const params = new HttpParams().set('name', name);
    return this.http.get<Employee[]>(`${this.apiUrl}/search`, { params });
  }

  /**
   * Get employees by department
   */
  getEmployeesByDepartment(department: string): Observable<Employee[]> {
    const params = new HttpParams().set('department', department);
    return this.http.get<Employee[]>(`${this.apiUrl}/department`, { params });
  }

  /**
   * Get employees by role
   */
  getEmployeesByRole(roleId: number): Observable<Employee[]> {
    return this.http.get<Employee[]>(`${this.apiUrl}/role/${roleId}`);
  }

  /**
   * Check if email is available
   */
  isEmailAvailable(email: string): Observable<boolean> {
    const params = new HttpParams().set('email', email);
    return this.http.get<boolean>(`${this.apiUrl}/check-email`, { params });
  }
}