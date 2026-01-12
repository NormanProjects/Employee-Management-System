export interface Employee {
  employeeId?: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  department?: string;
  position?: string;
  hireDate?: string;
  salary?: number;
  roleId: number;
  roleName?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateEmployeeRequest {
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  department?: string;
  position?: string;
  hireDate?: string;
  salary?: number;
  roleId: number;
}

export interface UpdateEmployeeRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  department?: string;
  position?: string;
  hireDate?: string;
  salary?: number;
  roleId?: number;
}