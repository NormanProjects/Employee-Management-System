package com.mashit.employeemanagementsystembackend.entity;
//Jakarta Persistence API - This is JPA (part of Jakarta EE, successor to Java EE)
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
// Lombok annotations - Reduces boilerplate code
import lombok.AllArgsConstructor;  // Creates constructor with all fields
import lombok.Data;                // Creates getters, setters, toString, equals, hashCode
import lombok.NoArgsConstructor;   // Creates constructor with no parameters
import java.util.List;

@Table(name = "roles")
@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity

public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id") //Maps to the column name
    private Long roleId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

}
