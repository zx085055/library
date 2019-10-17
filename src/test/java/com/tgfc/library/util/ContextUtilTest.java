package com.tgfc.library.util;

import com.tgfc.library.response.EmployeeResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ContextUtilTest {
    @Mock
    ContextUtil util;

    @Mock
    SecurityContext context;

    @Mock
    Authentication authentication;

    @BeforeEach
    public void init(){
        SecurityContextHolder.setContext(context);
    }

    @Test
    public void testGetContext(){
        Assertions.assertEquals(context, util.getContext());
    }

    @Test
    public void testGetPrincipal(){
        Mockito.when(context.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn("test");
        Assertions.assertEquals("test", util.getPrincipal());
    }

    @Test
    public void testGetAccount(){
        EmployeeResponse response = new EmployeeResponse();
        response.setAccount("test");
        Mockito.when(context.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(response);

        Assertions.assertEquals("TEST", util.getAccount());
    }

}
