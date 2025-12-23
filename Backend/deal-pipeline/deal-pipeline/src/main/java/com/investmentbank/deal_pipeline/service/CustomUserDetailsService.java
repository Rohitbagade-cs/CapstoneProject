package com.investmentbank.deal_pipeline.service;


import com.investmentbank.deal_pipeline.entity.User;
import com.investmentbank.deal_pipeline.repository.UserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username));

        if (!user.isEnabled()) {
            throw new DisabledException("User is disabled");
        }

//        return new org.springframework.security.core.userdetails.User(
//                user.getUsername(),
//                user.getPassword(), // 🔐 BCrypt password from DB
//                user.isEnabled(),   // enabled
//                true,               // accountNonExpired
//                true,               // credentialsNonExpired
//                true,               // accountNonLocked
//                List.of(
//                        new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())
//                )
//        );
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().getName())
                .build();
    }
}


