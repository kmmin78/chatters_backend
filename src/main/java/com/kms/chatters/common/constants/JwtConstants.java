package com.kms.chatters.common.constants;

public class JwtConstants {
    public static final String SECRET = "dev.mskim";
    public static final long EXPIRATION_TIME = 1 * 1 * 60 * 60 * 1000; // 1일
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}