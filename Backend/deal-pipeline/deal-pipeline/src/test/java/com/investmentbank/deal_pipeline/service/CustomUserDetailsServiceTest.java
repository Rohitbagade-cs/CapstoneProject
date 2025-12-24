package com.investmentbank.deal_pipeline.service;

import com.investmentbank.deal_pipeline.entity.Role;
import com.investmentbank.deal_pipeline.entity.User;
import com.investmentbank.deal_pipeline.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService service;

    @Test
    void loadUser_success() {
        Role role = new Role();
        role.setName("ADMIN");

        User user = new User();
        user.setUsername("admin");
        user.setPassword("pass");
        user.setRole(role);
        user.setEnabled(true);

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.of(user));

        assertNotNull(service.loadUserByUsername("admin"));
    }

    @Test
    void loadUser_disabled_exception() {
        User user = new User();
        user.setEnabled(false);

        when(userRepository.findByUsername("x"))
                .thenReturn(Optional.of(user));

        assertThrows(DisabledException.class,
                () -> service.loadUserByUsername("x"));
    }

    @Test
    void loadUser_notFound_exception() {
        when(userRepository.findByUsername("y"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("y"));
    }
}
