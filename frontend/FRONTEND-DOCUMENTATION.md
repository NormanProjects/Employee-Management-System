# Employee Management System - Frontend Documentation

---

## Overview

Angular-based frontend for the Employee Management System featuring:
- JWT-based authentication
- Role-based access control (ADMIN, MANAGER, EMPLOYEE)
- Employee CRUD operations
- Leave request management
- Responsive design
- Type-safe with TypeScript

---

## 🛠️ Tech Stack

### Core
- **Angular**: 19.x
- **TypeScript**: 5.x
- **RxJS**: 7.x

### Development
- **Angular CLI**: 19.x
- **Node.js**: 18+
- **npm**: 9+

### Styling
- **CSS3**: Custom styling
- **Responsive Design**: Mobile-first approach

---

## Project Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── core/                      # Singleton services
│   │   │   ├── guards/
│   │   │   │   ├── auth.guard.ts     # Authentication guard
│   │   │   │   └── role.guard.ts     # Role-based guard
│   │   │   ├── interceptors/
│   │   │   │   └── jwt.interceptor.ts # JWT token interceptor
│   │   │   └── services/
│   │   │       └── auth.service.ts   # Authentication service
│   │   │
│   │   ├── shared/                    # Shared resources
│   │   │   ├── models/
│   │   │   │   ├── auth.model.ts
│   │   │   │   ├── employee.model.ts
│   │   │   │   ├── role.model.ts
│   │   │   │   ├── user.model.ts
│   │   │   │   └── leave-request.model.ts
│   │   │   ├── services/
│   │   │   │   ├── employee.service.ts
│   │   │   │   ├── role.service.ts
│   │   │   │   ├── user.service.ts
│   │   │   │   └── leave-request.service.ts
│   │   │   └── components/
│   │   │       └── unauthorized/
│   │   │
│   │   ├── features/                  # Feature modules
│   │   │   ├── auth/
│   │   │   │   └── login/
│   │   │   ├── dashboard/
│   │   │   ├── employees/
│   │   │   │   ├── employee-list/
│   │   │   │   ├── employee-create/
│   │   │   │   ├── employee-edit/
│   │   │   │   └── employee-detail/
│   │   │   └── leave-requests/
│   │   │       ├── leave-request-list/
│   │   │       └── leave-request-create/
│   │   │
│   │   ├── app.component.ts           # Root component
│   │   ├── app.routes.ts              # Route configuration
│   │   └── app.config.ts              # App configuration
│   │
│   ├── environments/
│   │   ├── environment.ts             # Production config
│   │   └── environment.development.ts # Development config
│   │
│   ├── index.html                     # Main HTML
│   ├── main.ts                        # Bootstrap file
│   └── styles.css                     # Global styles
│
├── angular.json                       # Angular configuration
├── tsconfig.json                      # TypeScript configuration
├── package.json                       # Dependencies
└── README.md                          # Project README
```

---

## Architecture

### Layered Architecture

```
┌─────────────────────────────────────┐
│  PRESENTATION LAYER                 │
│  (Components)                       │
│  - Login                            │
│  - Dashboard                        │
│  - Employee List/Create/Edit        │
│  - Leave Request Management         │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│  BUSINESS LOGIC LAYER               │
│  (Services)                         │
│  - AuthService                      │
│  - EmployeeService                  │
│  - LeaveRequestService              │
│  - RoleService                      │
│  - UserService                      │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│  SECURITY LAYER                     │
│  (Guards & Interceptors)            │
│  - JWT Interceptor                  │
│  - Auth Guard                       │
│  - Role Guard                       │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│  HTTP CLIENT                        │
│  (Angular HttpClient)               │
│  → Backend API (Spring Boot)        │
└─────────────────────────────────────┘
```

### Request Flow

```
1. User Action (Click button, submit form)
        ↓
2. Component Method Called
        ↓
3. Calls Service Method
        ↓
4. HTTP Request Created
        ↓
5. JWT Interceptor Adds Token
        ↓
6. Request Sent to Backend
        ↓
7. Backend Processes & Responds
        ↓
8. Response Flows Back
        ↓
9. Service Returns Observable
        ↓
10. Component Updates UI
```

---

## Components

### 1. Authentication Components

#### **LoginComponent**
- **Path**: `features/auth/login`
- **Purpose**: User authentication
- **Features**:
  - Form validation (username, password)
  - Error handling
  - Loading state
  - JWT token storage
  - Redirect after login

**Key Methods:**
```typescript
onSubmit(): void          // Handle login
hasError(): boolean       // Check field errors
getErrorMessage(): string // Get validation message
```

---

### 2. Dashboard Component

#### **DashboardComponent**
- **Path**: `features/dashboard`
- **Purpose**: Landing page after login
- **Features**:
  - Role-based statistics (ADMIN/MANAGER)
  - Quick action cards
  - Personalized greeting
  - Navigation shortcuts

**Key Methods:**
```typescript
loadStats(): void           // Load dashboard statistics
hasRole(): boolean          // Check user role
goToEmployees(): void       // Navigate to employees
goToLeaveRequests(): void   // Navigate to leave requests
logout(): void              // Logout user
```

**Stats Displayed:**
- Total Employees
- Pending Leave Requests
- Departments

---

### 3. Employee Management Components

#### **EmployeeListComponent**
- **Path**: `features/employees/employee-list`
- **Purpose**: Display all employees
- **Features**:
  - Search by name/email/position
  - Filter by department
  - View/Edit/Delete actions
  - Role-based buttons
  - Responsive table

**Key Methods:**
```typescript
loadEmployees(): void         // Fetch all employees
filterEmployees(): void       // Apply search/filter
resetFilters(): void          // Clear filters
editEmployee(id): void        // Navigate to edit
deleteEmployee(id): void      // Delete employee
canManageEmployees(): boolean // Check permissions
```

#### **EmployeeCreateComponent**
- **Path**: `features/employees/employee-create`
- **Purpose**: Add new employee
- **Features**:
  - Reactive form with validation
  - Role dropdown (loads from backend)
  - Success/Error messages
  - Auto-redirect after save

**Form Fields:**
```typescript
firstName: string     // Required, min 2 chars
lastName: string      // Required, min 2 chars
email: string         // Required, valid email
phoneNumber: string   // Optional
department: string    // Optional
position: string      // Optional
roleId: number        // Required
hireDate: date        // Optional
salary: number        // Optional, min 0
```

#### **EmployeeEditComponent**
- **Path**: `features/employees/employee-edit`
- **Purpose**: Modify existing employee
- **Features**:
  - Loads employee data by ID
  - Pre-populates form
  - Same validation as create
  - Handles 404 errors

#### **EmployeeDetailComponent**
- **Path**: `features/employees/employee-detail`
- **Purpose**: View employee details
- **Features**:
  - Read-only view
  - Edit/Delete buttons (role-based)
  - Displays all employee info
  - System information (created/updated)

---

### 4. Leave Request Components

#### **LeaveRequestListComponent**
- **Path**: `features/leave-requests/leave-request-list`
- **Purpose**: Manage leave requests
- **Features**:
  - View all requests (ADMIN/MANAGER) or own (EMPLOYEE)
  - Filter by status (Pending, Approved, Rejected)
  - Approve/Reject (MANAGER/ADMIN only)
  - Cancel own requests
  - Status badges with colors

**Key Methods:**
```typescript
loadLeaveRequests(): void       // Load requests based on role
filterByStatus(): void          // Filter by status
approveRequest(id): void        // Approve leave request
rejectRequest(id): void         // Reject with reason
cancelRequest(id): void         // Cancel own request
canApprove(): boolean           // Check approval permission
```

#### **LeaveRequestCreateComponent**
- **Path**: `features/leave-requests/leave-request-create`
- **Purpose**: Submit new leave request
- **Features**:
  - Leave type dropdown (SICK, VACATION, etc.)
  - Date range picker
  - Reason textarea
  - Date validation (end > start)

**Form Fields:**
```typescript
leaveType: LeaveType  // Required (enum)
startDate: date       // Required
endDate: date         // Required
reason: string        // Required, min 10 chars
```

---

## Services

### 1. AuthService
**Location**: `core/services/auth.service.ts`

**Purpose**: Handle authentication and authorization

**Key Methods:**
```typescript
login(credentials): Observable<LoginResponse>
  // Login user, store token

logout(): void
  // Clear token, redirect to login

register(userData): Observable<LoginResponse>
  // Register new user

getToken(): string | null
  // Get stored JWT token

getCurrentUser(): LoginResponse | null
  // Get logged-in user info

isAuthenticated(): boolean
  // Check if user is logged in

hasRole(role: string): boolean
  // Check if user has specific role

hasAnyRole(roles: string[]): boolean
  // Check if user has any of roles

getUserId(): number | null
  // Get user ID

getEmployeeId(): number | null
  // Get employee ID
```

**Storage:**
- `localStorage.setItem('auth_token', token)`
- `localStorage.setItem('current_user', JSON.stringify(user))`

---

### 2. EmployeeService
**Location**: `shared/services/employee.service.ts`

**Purpose**: Employee CRUD operations

**API Endpoints:**
```typescript
GET    /api/employees              → getAllEmployees()
GET    /api/employees/{id}         → getEmployeeById(id)
POST   /api/employees              → createEmployee(data)
PUT    /api/employees/{id}         → updateEmployee(id, data)
DELETE /api/employees/{id}         → deleteEmployee(id)
GET    /api/employees/search       → searchEmployeesByName(name)
GET    /api/employees/department   → getEmployeesByDepartment(dept)
```

---

### 3. LeaveRequestService
**Location**: `shared/services/leave-request.service.ts`

**Purpose**: Leave request management

**API Endpoints:**
```typescript
GET    /api/leave-requests                    → getAllLeaveRequests()
GET    /api/leave-requests/{id}               → getLeaveRequestById(id)
POST   /api/leave-requests                    → createLeaveRequest(data)
PUT    /api/leave-requests/{id}               → updateLeaveRequest(id, data)
DELETE /api/leave-requests/{id}               → deleteLeaveRequest(id)
GET    /api/leave-requests/employee/{id}      → getLeaveRequestsByEmployee(id)
GET    /api/leave-requests/status             → getLeaveRequestsByStatus(status)
PUT    /api/leave-requests/{id}/approve       → approveLeaveRequest(id, data)
PUT    /api/leave-requests/{id}/reject        → rejectLeaveRequest(id, data)
PUT    /api/leave-requests/{id}/cancel        → cancelLeaveRequest(id)
GET    /api/leave-requests/pending            → getPendingLeaveRequests()
```

---

### 4. RoleService
**Location**: `shared/services/role.service.ts`

**API Endpoints:**
```typescript
GET    /api/roles              → getAllRoles()
GET    /api/roles/{id}         → getRoleById(id)
POST   /api/roles              → createRole(data)
PUT    /api/roles/{id}         → updateRole(id, data)
DELETE /api/roles/{id}         → deleteRole(id)
```

---

### 5. UserService
**Location**: `shared/services/user.service.ts`

**API Endpoints:**
```typescript
GET    /api/users                       → getAllUsers()
GET    /api/users/{id}                  → getUserById(id)
POST   /api/users                       → createUser(data)
PUT    /api/users/{id}                  → updateUser(id, data)
DELETE /api/users/{id}                  → deleteUser(id)
PUT    /api/users/{id}/change-password  → changePassword(id, data)
PUT    /api/users/{id}/activate         → activateUser(id)
PUT    /api/users/{id}/deactivate       → deactivateUser(id)
```

---

## Guards & Interceptors

### 1. JWT Interceptor
**Location**: `core/interceptors/jwt.interceptor.ts`

**Purpose**: Automatically attach JWT token to HTTP requests

**Functionality:**
```typescript
// Adds Authorization header to every request
Authorization: Bearer <token>

// Handles errors:
- 401 Unauthorized → Logout and redirect to login
- 403 Forbidden → Redirect to unauthorized page
```

**Usage:**
```typescript
// Registered in app.config.ts
provideHttpClient(
  withInterceptors([jwtInterceptor])
)
```

---

### 2. Auth Guard
**Location**: `core/guards/auth.guard.ts`

**Purpose**: Protect routes from unauthenticated users

**Functionality:**
```typescript
canActivate(route, state) {
  if (!isAuthenticated()) {
    router.navigate(['/login'], { 
      queryParams: { returnUrl: state.url } 
    });
    return false;
  }
  return true;
}
```

**Usage:**
```typescript
{
  path: 'dashboard',
  component: DashboardComponent,
  canActivate: [authGuard]  // Protected route
}
```

---

### 3. Role Guard
**Location**: `core/guards/role.guard.ts`

**Purpose**: Restrict routes based on user role

**Functionality:**
```typescript
canActivate(route, state) {
  const requiredRoles = route.data['roles'];
  
  if (!hasAnyRole(requiredRoles)) {
    router.navigate(['/unauthorized']);
    return false;
  }
  return true;
}
```

**Usage:**
```typescript
{
  path: 'employees',
  component: EmployeeListComponent,
  canActivate: [authGuard, roleGuard],
  data: { roles: ['ADMIN', 'MANAGER'] }  // Only ADMIN & MANAGER
}
```

---

## Models

### Authentication Models
**Location**: `shared/models/auth.model.ts`

```typescript
interface LoginRequest {
  username: string;
  password: string;
}

interface LoginResponse {
  token: string;
  userId: number;
  username: string;
  email: string;
  roleName: string;
  employeeId?: number;
}

interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  employeeId?: number;
  roleId: number;
}
```

---

### Employee Models
**Location**: `shared/models/employee.model.ts`

```typescript
interface Employee {
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

interface CreateEmployeeRequest {
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

interface UpdateEmployeeRequest {
  // All fields optional for partial updates
  firstName?: string;
  lastName?: string;
  // ... other fields
}
```

---

### Leave Request Models
**Location**: `shared/models/leave-request.model.ts`

```typescript
interface LeaveRequest {
  leaveRequestId?: number;
  employeeId: number;
  employeeName?: string;
  leaveType: LeaveType;
  startDate: string;
  endDate: string;
  reason?: string;
  status: LeaveStatus;
  approvedBy?: number;
  approverName?: string;
  approvedAt?: string;
  rejectionReason?: string;
  createdAt?: string;
  updatedAt?: string;
}

enum LeaveType {
  SICK = 'SICK',
  VACATION = 'VACATION',
  PERSONAL = 'PERSONAL',
  MATERNITY = 'MATERNITY',
  PATERNITY = 'PATERNITY',
  UNPAID = 'UNPAID'
}

enum LeaveStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  CANCELLED = 'CANCELLED'
}
```

---

## 🛣️ Routing

**Location**: `app.routes.ts`

### Route Configuration

```typescript
// Public Routes
/login                           → LoginComponent
/unauthorized                    → UnauthorizedComponent

// Protected Routes (Authenticated)
/dashboard                       → DashboardComponent
/leave-requests                  → LeaveRequestListComponent (All)
/leave-requests/create           → LeaveRequestCreateComponent (All)

// Protected Routes (ADMIN & MANAGER Only)
/employees                       → EmployeeListComponent
/employees/create                → EmployeeCreateComponent
/employees/edit/:id              → EmployeeEditComponent
/employees/:id                   → EmployeeDetailComponent

// Default
/                                → Redirect to /login
/**                              → Redirect to /login
```


## Change Log

### Version 1.0.0 (Initial Release)
**Features:**
- ✅ Authentication (Login)
- ✅ Dashboard with statistics
- ✅ Employee CRUD operations
- ✅ Leave request management
- ✅ Role-based access control
- ✅ Responsive design

**Security:**
- ✅ JWT authentication
- ✅ HTTP interceptors
- ✅ Route guards
- ✅ Role-based authorization

---

## 👥 Contributors

- **Developer**: Ntokozo Mashia
- **Repository**: https://github.com/NormanProjects/employee-management-system

---

## 🎯 Future Enhancements

- [ ] User profile page
- [ ] Advanced search and filtering
- [ ] Export to Excel/PDF
- [ ] Email notifications
- [ ] Dark mode
- [ ] Multi-language support
- [ ] File upload (documents, photos)
- [ ] Real-time updates (WebSocket)
- [ ] Mobile app (Ionic/React Native)

---

