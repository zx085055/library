package com.tgfc.library.security;

import com.tgfc.library.entity.Employee;
import com.tgfc.library.repository.IEmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tw.tgfc.common.spring.ldap.model.LdapUser;
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

    @Mock
    private PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    public void testEmployeeNotExists() {
        Mockito.when(authentication.getPrincipal()).thenReturn("ROOT");
        Mockito.when(authentication.getCredentials()).thenReturn("12345678");
        Mockito.when(employeeRepository.existsById("111")).thenReturn(false);
        Assertions.assertThrows(BadCredentialsException.class, () ->ldapAuthProvider.authenticate(authentication));
    }

    @Test
    public void testEmployeeExists() {
        Mockito.when(authentication.getPrincipal()).thenReturn("tgfc041");
        Mockito.when(authentication.getCredentials()).thenReturn("Tgfc@3B1234");
        Mockito.when(employeeRepository.existsById("tgfc041")).thenReturn(true);
        Mockito.when(encoder.matches("Tgfc@3B1234","{bcrypt}$2a$10$4uCp7zEVHIsD7I4Msh1g7eR4IsWTeI8nwn58l8TLv3O2V7z3h91DK")).thenReturn(true);
        Employee loginUserData = new Employee();
        loginUserData.setId("tgfc041");
        loginUserData.setDepartment("研發部");
        loginUserData.setEmail("tgfc041@tgfc.tw");
        loginUserData.setName("魏浩恩");
        loginUserData.setPassword("{bcrypt}$2a$10$4uCp7zEVHIsD7I4Msh1g7eR4IsWTeI8nwn58l8TLv3O2V7z3h91DK");
        Mockito.when(employeeRepository.getOne(authentication.getPrincipal().toString())).thenReturn(loginUserData);
        Assertions.assertTrue(ldapAuthProvider.authenticate(authentication).isAuthenticated());
    }

    @Test
    public void testEmployeeWrongPassword() {
        Mockito.when(authentication.getPrincipal()).thenReturn("tgfc041");
        Mockito.when(authentication.getCredentials()).thenReturn("12345678");
        Mockito.when(employeeRepository.existsById("tgfc041")).thenReturn(true);
        Mockito.when(encoder.matches("tgfc041","{bcrypt}$2a$10$WodINMg6UpNWbOSmYgOAseYGlAUXfxwG7DXHiudLfkQBiv2zfRz8a")).thenReturn(true);
        Employee loginUserData = new Employee();
        loginUserData.setId("tgfc041");
        loginUserData.setDepartment("研發部");
        loginUserData.setEmail("tgfc041@tgfc.tw");
        loginUserData.setName("魏浩恩");
        loginUserData.setPassword("{bcrypt}$2a$10$WodINMg6UpNWbOSmYgOAseYGlAUXfxwG7DXHiudLfkQBiv2zfRz8a");
        Mockito.when(employeeRepository.getOne(authentication.getPrincipal().toString())).thenReturn(loginUserData);
        Assertions.assertThrows(BadCredentialsException.class, () ->ldapAuthProvider.authenticate(authentication)).getMessage();

    }

    @Test
    public void testSupport(){
        Assertions.assertTrue(ldapAuthProvider.supports(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testAddData(){
        Mockito.when(authentication.getPrincipal()).thenReturn("tgfc041");
        Mockito.when(authentication.getCredentials()).thenReturn("Tgfc@3B1234");
        Mockito.when(employeeRepository.existsById("111")).thenReturn(false);
        LdapUser ldapUser = new LdapUser();
        ldapUser.setAccount("swei");
        ldapUser.setName("魏浩恩");
        ldapUser.setEmail("tgfc041@tgfc.tw");
        Mockito.when(ldapService.authenticate(authentication.getPrincipal().toString(),authentication.getCredentials().toString())).thenReturn(ldapUser);

        Employee test = new Employee();
        test.setId("tgfc041");
        test.setName("swei");
        test.setEmail("tgfc041@tgfc.tw");
        test.setDepartment("研發部");
        test.setPassword("{bcrypt}$2a$10$4uCp7zEVHIsD7I4Msh1g7eR4IsWTeI8nwn58l8TLv3O2V7z3h91DK");
        Mockito.when(employeeRepository.save(test)).thenReturn(test);
        Assertions.assertNotNull(employeeRepository.save(test));
        Assertions.assertTrue(ldapAuthProvider.authenticate(authentication).isAuthenticated());
    }
}
