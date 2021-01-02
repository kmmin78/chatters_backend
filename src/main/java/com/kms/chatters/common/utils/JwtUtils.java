package com.kms.chatters.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.kms.chatters.auth.vo.UserDetailsImpl;
import com.kms.chatters.common.constants.JwtConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	//token 생성
	public String generateJwtToken(Authentication authentication) throws UnsupportedEncodingException {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		// HashMap<String, Object> headers = new HashMap<>();
		// headers.put("typ", "JWT");
		// headers.put("alg", "HS512");
		//HashMap<String, Object> claims = new HashMap<>();
		//claims.put("role", userPrincipal.getAuthorities());

		Date now = new Date();

		return Jwts.builder()
				.setSubject((userPrincipal.getUsername()))
				//.setHeader(headers)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + JwtConstants.EXPIRATION_TIME))
				.claim("test", "test body")
				//.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, JwtConstants.SECRET.getBytes("UTF-8"))
				.compact();
	}

	//토큰 -> username 추출
	public String getUserNameFromJwtToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
        return Jwts
                .parser()
                .setSigningKey(JwtConstants.SECRET.getBytes("UTF-8"))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
	}

	//token 유효성 체크
	public String validateJwtToken(String authToken) {
		try {
			// Jws<Claims> claims = 
			Jwts
				.parser()
				.setSigningKey(JwtConstants.SECRET.getBytes("UTF-8"))
				.parseClaimsJws(authToken);
			// String scope = (String) claims.getBody().get("role");
			return "OK";
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
			return "EXPIRED";
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
			return "INVALID_SIGNATURE";
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
			return "INVALID_TOKEN";
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
			return "UNSUPPORTED";
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
			return "JWT_CLAIMS_EMPTY";
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncoding : {}", e.getMessage());
			return "UNSUPPORTED_ENCODING";
		}
	}
}
