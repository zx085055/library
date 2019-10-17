package com.tgfc.library.security;

import com.tgfc.library.entity.Employee;
import com.tgfc.library.enums.PermissionEnum;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.response.EmployeeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tw.tgfc.common.spring.ldap.model.LdapUser;
import tw.tgfc.common.spring.ldap.service.LDAPService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Transactional
public class LdapAuthProvider implements AuthenticationProvider {

    @Autowired
    LDAPService ldapService;

    @Autowired
    IEmployeeRepository employeeRepository;

    private PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = authentication.getPrincipal().toString();//讀取輸入的帳號
        String password = authentication.getCredentials().toString();//讀取輸入的密碼
        Employee loginUser = null;//宣告一個變數用來存使用者的資料
        if (!employeeRepository.existsById(account)) { //如果帳號不存在的話
            loginUser = findUserByLDAp(account, password);
        } else {//如果帳號存在
            loginUser = employeeRepository.getOne(account);//讀取所有該帳號的相關資料
            if (!encoder.matches(password, loginUser.getPassword())) {//比對密碼是否相符
                throw new BadCredentialsException("請確認帳號或密碼");//如果不相符會丟出一個錯誤訊息
            }
        }
        String[] permissions = getPermissionsList(loginUser);
        EmployeeResponse principle = EmployeeResponse.valueOf(loginUser);
        principle.setPermissions(Arrays.asList(permissions));
        return new UsernamePasswordAuthenticationToken(principle, null, AuthorityUtils.createAuthorityList(permissions));
    }

    private String[] getPermissionsList(Employee loginUser) {
        List<String> permissions = new ArrayList<>();

        if (loginUser.getDepartment().equals("管理部")) {
            permissions.add(PermissionEnum.ROLE_ADMIN.name());
        }
        permissions.add(PermissionEnum.ROLE_USER.name());
        return permissions.toArray(new String[0]);
    }

    private Employee findUserByLDAp(String account, String password) {
        LdapUser ldapUser = ldapService.authenticate(account, password);
        if (ldapUser == null) {
            throw new BadCredentialsException("請確認帳號或密碼");
        }
        return addUserToDataBase(ldapUser, password);
    }

    private Employee addUserToDataBase(LdapUser ldapUser, String password) {
        Employee traceUser = new Employee();
        traceUser.setId(ldapUser.getAccount());
        traceUser.setPassword(encoder.encode(password));
        traceUser.setName(ldapUser.getName());
        traceUser.setEmail(ldapUser.getEmail());
        traceUser.setDepartment(ldapUser.getDepartment());
        employeeRepository.save(traceUser);
        return traceUser;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == UsernamePasswordAuthenticationToken.class;
    }
}
