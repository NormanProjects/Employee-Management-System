import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

/**
 * Role Guard - Checks if user has specific role(s)
 * Usage: Add to route data: { roles: ['ADMIN', 'MANAGER'] }
 */
export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Get required roles from route data
  const requiredRoles = route.data['roles'] as string[];

  if (!requiredRoles || requiredRoles.length === 0) {
    // No roles specified, allow access
    return true;
  }

  // Check if user has any of the required roles
  if (authService.hasAnyRole(requiredRoles)) {
    return true;
  }

  // User doesn't have required role
  console.warn(`Access denied. Required roles: ${requiredRoles.join(', ')}`);
  router.navigate(['/unauthorized']);
  return false;
};