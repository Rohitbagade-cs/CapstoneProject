package com.investmentbank.deal_pipeline.service;

import com.investmentbank.deal_pipeline.dto.CreateUserRequest;
import com.investmentbank.deal_pipeline.entity.Role;
import com.investmentbank.deal_pipeline.entity.User;
import com.investmentbank.deal_pipeline.repository.RoleRepository;
import com.investmentbank.deal_pipeline.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsers_success() {
        Role role = new Role();
        role.setName("ADMIN");

        User user = new User();
        user.setRole(role);

        when(userRepository.findAll()).thenReturn(List.of(user));

        assertEquals(1, userService.getAllUsers().size());
    }

    @Test
    void createUser_success() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("123");
        req.setRole("ADMIN");

        Role role = new Role();
        role.setName("ADMIN");

        when(userRepository.existsByUsername("test")).thenReturn(false);
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        userService.createUser(req);
        verify(userRepository).save(any());
    }

    @Test
    void createUser_duplicateUsername_exception() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");

        when(userRepository.existsByUsername("test")).thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> userService.createUser(req));
    }

    @Test
    void createUser_invalidRole_exception() {
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setRole("ADMIN");

        when(userRepository.existsByUsername("test")).thenReturn(false);
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.createUser(req));
    }

    @Test
    void deactivateUser_success() {
        User user = new User();
        user.setEnabled(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deactivateUser(1L);
        assertFalse(user.isEnabled());
    }

    @Test
    void activateUser_success() {
        User user = new User();
        user.setEnabled(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.activateUser(1L);
        assertTrue(user.isEnabled());
    }

    @Test
    void activateUser_notFound_exception() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> userService.activateUser(1L));
    }
}
