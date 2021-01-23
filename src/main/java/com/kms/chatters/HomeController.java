package com.kms.chatters;

import java.util.HashMap;

import com.kms.chatters.auth.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @Autowired
    UserMapper um;

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        UserDetails userDetails =
        (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("username", userDetails.getUsername());
        result.put("password", userDetails.getPassword());
        result.put("role", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        result.put("role2", userDetails.getAuthorities());
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}