import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/login/login';
import { DashboardComponent } from './features/dashboard/dashboard';
import { UnauthorizedComponent } from './shared/components/unauthorized/unauthorized';
import { EmployeeListComponent } from './features/employees/employee-list/employee-list';
import { authGuard } from './core/guards/auth-guard';
import { Component } from '@angular/core';
import { roleGuard } from './core/guards/role-guard';
import { EmployeeCreateComponent } from './features/employees/employee-create/employee-create';
import { EmployeeEditComponent } from './features/employees/employee-edit/employee-edit';
import { EmployeeDetailComponent } from './features/employees/employee-detail/employee-detail';
import { LeaveRequestCreateComponent } from './features/leave-requests/leave-request-create/leave-request-create';
import { LeaveRequestListComponent } from './features/leave-requests/leave-request-list/leave-request-list';

export const routes: Routes = [
     // Public routes
  {
    path: 'login',
    component: LoginComponent
  },
     // Protected routes
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [authGuard]  
  },
     // Employee routes (ADMIN & MANAGER only)s
  {
    path:'employees',
    component: EmployeeListComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['Admin', 'Manager'] }
  },
  {
  path: 'employees/:id',
  component: EmployeeDetailComponent,
  canActivate: [authGuard, roleGuard],
  data: { roles: ['ADMIN', 'MANAGER'] }
  },
  {
    path: 'employees/create',
    component: EmployeeCreateComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['Admin', 'Manager'] }
  },
  {
    path: 'employees/edit/:id',
    component: EmployeeEditComponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'MANAGER'] }
  },
     // Leave Request routes (All authenticated users)
  {
    path: 'leave-requests',
    component: LeaveRequestListComponent,
    canActivate: [authGuard]
  },
  {
    path: 'leave-requests/create',
    component: LeaveRequestCreateComponent,
    canActivate: [authGuard]
  },
  {
    path: 'unauthorized',
    component: UnauthorizedComponent
  },
     // Default routes
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];