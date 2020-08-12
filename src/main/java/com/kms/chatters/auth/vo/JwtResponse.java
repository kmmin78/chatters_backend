package com.kms.chatters.auth.vo;

import java.util.List;

public class JwtResponse {
	private String token;
	private String type = "Bearer";
	private String username;
	private String member_name;
	private List<String> roles;

	public JwtResponse(String accessToken, String username, String member_name, List<String> roles) {
		this.token = accessToken;
		this.username = username;
		this.member_name = member_name;
		this.roles = roles;
	}

	public String getAccessToken() {
		return token;
	}

	public void setAccessToken(String accessToken) {
		this.token = accessToken;
	}

	public String getTokenType() {
		return type;
	}

	public void setTokenType(String tokenType) {
		this.type = tokenType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMemberName() {
		return member_name;
	}

	public void setMemberName(String member_name){
		this.member_name = member_name;
	}

	public List<String> getRoles() {
		return roles;
	}
}