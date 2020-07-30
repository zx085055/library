package com.tgfc.library.util;

import com.tgfc.library.response.EmployeeResponse;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


public class ContextUtil {

    public static SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

    public static Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getAccount() {
        return ((EmployeeResponse) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount().toUpperCase();

    }

    public static final PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
}
