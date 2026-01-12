package com.mashit.employeemanagementsystembackend.dto;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String username;
    private String employeeName;
    private String role;

    public JwtResponse(String token, Long userId, String username, String employeeName, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.employeeName = employeeName;
        this.role = role;
    }

    // Getters
    public String getToken() { return token; }
    public String getType() { return type; }
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmployeeName() { return employeeName; }
    public String getRole() { return role; }
}