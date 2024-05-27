package org.example.demojwt.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String password = "dummyPassword";
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Na potrzeby testów zwracamy domyślnego użytkownika
        return new User("staticUser", encoder.encode(password), new ArrayList<>());
    }
}
