package com.kms.chatters.common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.kms.chatters.auth.service.UserDetailsServiceImpl;
import com.kms.chatters.common.utils.JwtUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response, 
        FilterChain filterChain
        ) throws ServletException, IOException {

		//before doFilter
		String jwt = parseJwt(request);
		String validResult = "";
		//최초 로그인 시에는 jwt가 존재하지 않음.
		if(jwt != null){
			validResult = jwtUtils.validateJwtToken(jwt);
		}

		//유효한 jwt일 경우
		if (validResult.equals("OK")) {
			try {
				
				String username = jwtUtils.getUserNameFromJwtToken(jwt);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = 
					new UsernamePasswordAuthenticationToken(
						userDetails, 
						null, 
						userDetails.getAuthorities()
					);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
				
				// if you want to get UserDetails
				// UserDetails userDetails =
				// (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				// userDetails.getUsername()
				// userDetails.getPassword()
				// userDetails.getAuthorities()
				
			} catch (Exception e) {
				logger.error("Cannot set user authentication: {}", e);
			}
		//JWT TOKEN이 만료된 경우에는 바로 JSON 리턴.
		} else if (validResult.equals("EXPIRED")) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			Gson gsonObj = new Gson();
			HashMap<String, Object> resultMap = new HashMap<String, Object>();
			try {

				resultMap.put("JWT_RESULT", validResult);
				out.print(gsonObj.toJson(resultMap));

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				out.flush();
			}
			return;
		}

		filterChain.doFilter(request, response);

		//after doFilter
		
	}

	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		//token에 "" 붙어서 오면 제거해줘야함.
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7, headerAuth.length()).replaceAll("\"", "");
		}

		return null;
	}
}