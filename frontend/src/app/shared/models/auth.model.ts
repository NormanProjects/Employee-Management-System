export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  userId: number;
  username: string;
  email: string;
  roleName: string;
  employeeId?: number;
}

export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  employeeId?: number;
  roleId: number;
}