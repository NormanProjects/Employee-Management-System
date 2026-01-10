-- ============================
-- Roles
-- ============================
CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- ============================
-- Employees
-- ============================
CREATE TABLE employees (
    employee_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_employee_role
        FOREIGN KEY (role_id)
        REFERENCES roles(role_id)
);

-- ============================
-- Users (Authentication)
-- ============================
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    employee_id INT UNIQUE NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_user_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(employee_id)
);

-- ============================
-- Leave Requests
-- ============================
CREATE TABLE leave_requests (
    id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_leave_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(employee_id)
);
