package com.tgfc.library.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import tw.tgfc.common.spring.ldap.service.LDAPService;

public class LdapAuthProviderTest {

    LdapAuthProvider ldapAuthProvider = Mockito.mock(LdapAuthProvider.class);

    Authentication authentication = Mockito.mock(Authentication.class);

    @Mock
    LDAPService ldapService = Mockito.mock(LDAPService.class);

    @Test
    public void test() {
        Mockito.when(authentication.getPrincipal()).thenReturn("ROOT");
        Mockito.when(authentication.getCredentials()).thenReturn("12345678");

        ldapAuthProvider.authenticate(authentication);
    }
}
