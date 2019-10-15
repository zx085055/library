package com.tgfc.library.security;

import com.tgfc.library.repository.IEmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tw.tgfc.common.spring.ldap.service.LDAPService;

@ExtendWith(SpringExtension.class)
public class LdapAuthProviderTest {

    @InjectMocks
    LdapAuthProvider ldapAuthProvider ;

    @Mock
    Authentication authentication;

    @Mock
    IEmployeeRepository employeeRepository;

    @Mock
    LDAPService ldapService = Mockito.mock(LDAPService.class);

    @Test
    public void test() {
        Mockito.when(authentication.getPrincipal()).thenReturn("ROOT");
        Mockito.when(authentication.getCredentials()).thenReturn("12345678");
        Mockito.when(employeeRepository.existsById("111")).thenReturn(false);

        Assertions.assertThrows(BadCredentialsException.class, () ->ldapAuthProvider.authenticate(authentication));
    }
}
