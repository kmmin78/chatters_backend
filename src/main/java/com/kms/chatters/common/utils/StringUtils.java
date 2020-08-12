package com.kms.chatters.common.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {

    public String nvl(String origin, String replaced) {
        if(
           origin == null ||
           origin.equals("") ||
           origin.equals("undefined") ||
           origin.equals("null")
        ) {
            return replaced;
        } else {
            return origin;
        }
    }

    public boolean isEmpty(String origin) {
        if(
           origin == null ||
           origin.equals("") ||
           origin.equals("undefined") ||
           origin.equals("null")
        ) {
            return true;
        } else {
            return false;
        }
    }
    
}