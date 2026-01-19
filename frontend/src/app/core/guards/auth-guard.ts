import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  /* ----------------------------
   * Authentication Check
   * ---------------------------- */

  if (!authService.isAuthenticated()) {
    router.navigate(['/login'], {
      queryParams: { returnUrl: state.url }
    });
    return false;
  }

  /* ----------------------------
   * Role-Based Authorization
   * ---------------------------- */

  const requiredRoles = route.data?.['roles'] as string[] | undefined;

  if (requiredRoles?.length && !authService.hasAnyRole(requiredRoles)) {
    router.navigate(['/unauthorized']);
    return false;
  }

  return true;
};
