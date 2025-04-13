package com.tutorial.security.security.service;

import com.tutorial.security.security.repo.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    //we want to load our user from database
    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    //an interface having loadUserByUsername
    @Override
    @Transactional //when I load the user I want roles & authorities too with it
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //since email can uniquely find a user
        return repository.findByEmail(email)
                .orElseThrow( () -> new UsernameNotFoundException("User not in DB"));
    }
}
