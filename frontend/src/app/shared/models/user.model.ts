export interface User {
  userId?: number;
  username: string;
  email: string;
  employeeId?: number;
  employeeName?: string;
  roleId: number;
  roleName?: string;
  isActive?: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateUserRequest {
  username: string;
  password: string;
  email: string;
  employeeId?: number;
  roleId: number;
}

export interface UpdateUserRequest {
  username?: string;
  email?: string;
  employeeId?: number;
  roleId?: number;
  isActive?: boolean;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}