package com.tgfc.library.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


public class ContextUtil {
    private static Authentication authentication; //TODO 測試用

    public static SecurityContext getContext(){
        return SecurityContextHolder.getContext();
    }

    public static Object getPrincipal(){
        return  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Authentication getAuthentication() {//TODO 測試用
        return authentication;
    }

    public static void setAuthentication(Authentication authentication) {//TODO 測試用
        ContextUtil.authentication = authentication;
    }
}
