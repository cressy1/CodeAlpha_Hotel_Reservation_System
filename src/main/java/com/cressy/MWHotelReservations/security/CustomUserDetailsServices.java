package com.cressy.MWHotelReservations.security;

import com.cressy.MWHotelReservations.entity.Users;
import com.cressy.MWHotelReservations.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsServices implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(email).orElseThrow(
                ()-> new UsernameNotFoundException("User not found"));

        return new User(users.getEmail(), users.getPassword(), mapRolesToAuthorities(users));
    }

    public Collection<GrantedAuthority> mapRolesToAuthorities(Users users) {
        return Collections.singleton(new SimpleGrantedAuthority(String.valueOf(users.getRoles())));
    }
}
