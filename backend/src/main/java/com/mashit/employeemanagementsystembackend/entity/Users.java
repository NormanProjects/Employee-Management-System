package com.mashit.employeemanagementsystembackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;


    @Column(name = "enabled")
    private Boolean enabled = true;  // Default value


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", unique = true, nullable = false)
    private Employee employee;
}

