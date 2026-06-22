package com.example.crm.config.jwt;

import com.example.crm.entity.AppUser;
import com.example.crm.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AppUser user = userRepository.getUserWithRole(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("No User Found with username: " + username)
                );

        GrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(authority)
        );
    }
}
