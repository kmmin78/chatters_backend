package com.kms.chatters.common.constants;

public class JwtConstants {
    public static final String SECRET = "dev.mskim";
    public static final int EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7일
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}