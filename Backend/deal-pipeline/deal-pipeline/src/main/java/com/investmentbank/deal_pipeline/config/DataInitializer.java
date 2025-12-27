package com.investmentbank.deal_pipeline.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.investmentbank.deal_pipeline.repository.UserRepository;
import com.investmentbank.deal_pipeline.repository.RoleRepository;
import com.investmentbank.deal_pipeline.entity.User;
import com.investmentbank.deal_pipeline.entity.Role;


@Configuration
public class DataInitializer {

    @Bean
CommandLineRunner initUsers(UserRepository userRepository,
                            RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder) {

    return args -> {

        createUserIfNotExists(
            "ADMIN", "admin", "admin@test.com", "admin123",
            userRepository, roleRepository, passwordEncoder
        );

        createUserIfNotExists(
            "ANALYST", "analyst", "analyst@test.com", "analyst123",
            userRepository, roleRepository, passwordEncoder
        );
    };
}

private void createUserIfNotExists(String roleName,
                                   String username,
                                   String email,
                                   String rawPassword,
                                   UserRepository userRepository,
                                   RoleRepository roleRepository,
                                   PasswordEncoder passwordEncoder) {

    if (userRepository.existsByUsername(username)) {
        return;
    }

    Role role = roleRepository.findByName(roleName)
            .orElseGet(() -> {
                Role r = new Role();
                r.setName(roleName);
                return roleRepository.save(r);
            });

    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(rawPassword));
    user.setEnabled(true);
    user.setRole(role);

    userRepository.save(user);

    System.out.println("✅ Created user: " + username);
}
}