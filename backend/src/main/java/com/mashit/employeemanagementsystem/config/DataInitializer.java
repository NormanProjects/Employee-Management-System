package com.mashit.employeemanagementsystem.config;

import com.mashit.employeemanagementsystem.entity.Role;
import com.mashit.employeemanagementsystem.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        initializeRoles();
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            log.info("Initializing roles...");

            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);

            Role managerRole = new Role();
            managerRole.setName("MANAGER");
            roleRepository.save(managerRole);

            Role employeeRole = new Role();
            employeeRole.setName("EMPLOYEE");
            roleRepository.save(employeeRole);

            log.info("Roles initialized successfully: ADMIN, MANAGER, EMPLOYEE");
        } else {
            log.info("Roles already exist, skipping initialization");
        }
    }
}