package com.CalculatorMVCUpload.configuration;

import com.CalculatorMVCUpload.entity.users.UserEntity;
import com.CalculatorMVCUpload.service.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.findByLogin(username);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(userEntity);
    }

    public CustomUserDetails loadUserById(int id) throws UsernameNotFoundException {
        UserEntity userEntity = userService.findById(id);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(userEntity);
    }
}
