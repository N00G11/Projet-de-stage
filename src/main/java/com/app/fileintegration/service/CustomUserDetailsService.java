package com.app.fileintegration.service;


import com.app.fileintegration.entity.User;
import com.app.fileintegration.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw  new UsernameNotFoundException("USer not found with username:" + username);
        }else {
            return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));
        }
    }
}
