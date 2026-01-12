import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Check if user is authenticated
  if (!authService.isAuthenticated()) {
    // Not authenticated, redirect to login
    router.navigate(['/login'], { 
      queryParams: { returnUrl: state.url } 
    });
    return false;
  }

  // Check if route requires specific roles
  const requiredRoles = route.data['roles'] as string[];
  
  if (requiredRoles && requiredRoles.length > 0) {
    // Check if user has any of the required roles
    if (!authService.hasAnyRole(requiredRoles)) {
      // User doesn't have required role, redirect to unauthorized
      router.navigate(['/unauthorized']);
      return false;
    }
  }

  // User is authenticated and has required role
  return true;
};