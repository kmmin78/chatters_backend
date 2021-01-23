package com.kms.chatters.auth.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

import com.kms.chatters.auth.vo.JwtResponse;
import com.kms.chatters.auth.vo.LoginRequest;
import com.kms.chatters.auth.vo.UserDetailsImpl;
import com.kms.chatters.common.utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) throws UnsupportedEncodingException {
        Authentication authentication = 
                authenticationManager.authenticate(
				    new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), 
                        loginRequest.getPassword()
                    )
                );

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		//redis에 로그인 정보 저장 (username, jwt)
		//로그인 정보가 없다면 생성
		//로그인 정보가 존재한다면 update 하고 redis publish

		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getUsername(),
												 userDetails.getMemberName(), 
												 roles));
	}
}