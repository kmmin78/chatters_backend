package com.kms.chatters.common.constants;

public class JwtConstants {
    public static final String SECRET = "dev.mskim";
    // public static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7일
    public static final long EXPIRATION_TIME = 1 * 1 * 30 * 60 * 1000; // 30분
    // public static final long EXPIRATION_TIME = 1 * 1 * 1 * 10 * 1000; // 10초
    // public static final long EXPIRATION_TIME = 1 * 1 * 1 * 60 * 1000; // 60초
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}