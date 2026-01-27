package com.electronic.store.services.implimentation;

import com.electronic.store.entities.User;
import com.electronic.store.exception.ResourceNotFoundExc;
import com.electronic.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundExc("User of the given username does not exist!"));
        return user;
    }
}
