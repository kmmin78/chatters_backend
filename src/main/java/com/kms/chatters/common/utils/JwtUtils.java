package com.kms.chatters.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import com.kms.chatters.auth.vo.UserDetailsImpl;
import com.kms.chatters.common.constants.JwtConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
		//claim 생성
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("username", userPrincipal.getUsername());
		claims.put("roles", userPrincipal.getRoles());
		// ObjectMapper mapper = new ObjectMapper();
		// try {
		// 	role_claim.put("roles", mapper.writeValueAsString(userPrincipal.getAuthorities()));
		// } catch (JsonProcessingException e) {
		// 	e.printStackTrace();
		// }

		Date now = new Date();

		return Jwts.builder()
				.setSubject(userPrincipal.getUsername())
				//.setHeader(headers)
				.setIssuedAt(now)
				.setExpiration(new Date(System.currentTimeMillis() + JwtConstants.EXPIRATION_TIME))
				// .claim("roles", userPrincipal.getAuthorities().toString())
				.addClaims(claims)
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
				.get("username")
				.toString();
                // .getSubject();
	}

	//토큰 -> roles 추출
	public String getRolesFromJwtToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException, UnsupportedEncodingException {
		//roles를 key로 갖는 json string 추출
		return Jwts
				.parser()
				.setSigningKey(JwtConstants.SECRET.getBytes("UTF-8"))
				.parseClaimsJws(token)
				.getBody()
				.get("roles")
				.toString();

		// List<GrantedAuthority> authorities = new ArrayList<>();
		// ObjectMapper mapper = new ObjectMapper();
		// ArrayList<Map<String, String>> roles = null;
		
		// //json string에서 ArrayList<Map<String, String>> 추출
		// try {
		// 	roles = mapper.readValue(json, ArrayList.class);
		// } catch (JsonMappingException e) {
		// 	e.printStackTrace();
		// } catch (JsonProcessingException e) {
		// 	e.printStackTrace();
		// }
		
		// //authority 객체 생성 후 GrantedAuthority List 반환
		// //UsernamePasswordAuthenticationToken 생성 시 필요한 authority 객체임.
		// roles.forEach(
		// 	role -> {
		// 		GrantedAuthority authority = new SimpleGrantedAuthority(role.get("authority").toString());
		// 		authorities.add(authority);
		// 	}
		// );

		// return authorities;
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

	public String parseJwt(String token) {
		//token에 "" 붙어서 오면 제거해줘야함.
		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			return token.substring(7, token.length()).replaceAll("\"", "");
		}

		return null;
	}
}
