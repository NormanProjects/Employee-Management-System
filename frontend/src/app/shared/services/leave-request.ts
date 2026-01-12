import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { 
  LeaveRequest, 
  CreateLeaveRequestRequest, 
  ApproveLeaveRequest, 
  RejectLeaveRequest,
  LeaveStatus 
} from '../models/leave-request.model';

@Injectable({
  providedIn: 'root'
})
export class LeaveRequestService {
  private apiUrl = `${environment.apiUrl}/leave-requests`;

  constructor(private http: HttpClient) {}

  /**
   * Get all leave requests
   */
  getAllLeaveRequests(): Observable<LeaveRequest[]> {
    return this.http.get<LeaveRequest[]>(this.apiUrl);
  }

  /**
   * Get leave request by ID
   */
  getLeaveRequestById(id: number): Observable<LeaveRequest> {
    return this.http.get<LeaveRequest>(`${this.apiUrl}/${id}`);
  }

  /**
   * Create new leave request
   */
  createLeaveRequest(request: CreateLeaveRequestRequest): Observable<LeaveRequest> {
    return this.http.post<LeaveRequest>(this.apiUrl, request);
  }

  /**
   * Update leave request
   */
  updateLeaveRequest(id: number, request: CreateLeaveRequestRequest): Observable<LeaveRequest> {
    return this.http.put<LeaveRequest>(`${this.apiUrl}/${id}`, request);
  }

  /**
   * Delete leave request
   */
  deleteLeaveRequest(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Get leave requests by employee ID
   */
  getLeaveRequestsByEmployee(employeeId: number): Observable<LeaveRequest[]> {
    return this.http.get<LeaveRequest[]>(`${this.apiUrl}/employee/${employeeId}`);
  }

  /**
   * Get leave requests by status
   */
  getLeaveRequestsByStatus(status: LeaveStatus): Observable<LeaveRequest[]> {
    const params = new HttpParams().set('status', status);
    return this.http.get<LeaveRequest[]>(`${this.apiUrl}/status`, { params });
  }

  /**
   * Approve leave request
   */
  approveLeaveRequest(id: number, request: ApproveLeaveRequest): Observable<LeaveRequest> {
    return this.http.put<LeaveRequest>(`${this.apiUrl}/${id}/approve`, request);
  }

  /**
   * Reject leave request
   */
  rejectLeaveRequest(id: number, request: RejectLeaveRequest): Observable<LeaveRequest> {
    return this.http.put<LeaveRequest>(`${this.apiUrl}/${id}/reject`, request);
  }

  /**
   * Cancel leave request
   */
  cancelLeaveRequest(id: number): Observable<LeaveRequest> {
    return this.http.put<LeaveRequest>(`${this.apiUrl}/${id}/cancel`, {});
  }

  /**
   * Get pending leave requests (for managers/admins)
   */
  getPendingLeaveRequests(): Observable<LeaveRequest[]> {
    return this.http.get<LeaveRequest[]>(`${this.apiUrl}/pending`);
  }
}