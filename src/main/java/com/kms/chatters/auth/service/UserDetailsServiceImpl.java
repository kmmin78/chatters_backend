package com.kms.chatters.auth.service;

import com.kms.chatters.auth.mapper.UserMapper;
import com.kms.chatters.auth.vo.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            UserDetailsImpl userDetails = new UserDetailsImpl();
            try {
                userDetails = userMapper.getUserDetails(username);    
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(userDetails == null){
                throw new UsernameNotFoundException(username+" is not found!");
            }

            return userDetails;
    }
}