import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  // Get the token from AuthService
  const token = authService.getToken();
  
  // Clone the request and add Authorization header if token exists
  let authReq = req;
  if (token) {
    authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  
  // Handle the request and catch errors
  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // Handle 401 Unauthorized errors
      if (error.status === 401) {
        // Token is invalid or expired, logout user
        authService.logout();
        router.navigate(['/login']);
      }
      
      // Handle 403 Forbidden errors
      if (error.status === 403) {
        console.error('Access forbidden: You do not have permission');
        router.navigate(['/unauthorized']);
      }
      
      return throwError(() => error);
    })
  );
};