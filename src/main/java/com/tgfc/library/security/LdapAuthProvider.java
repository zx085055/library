package com.tgfc.library.security;

import com.tgfc.library.entity.Employee;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.response.EmployeeResponse;
import com.tgfc.library.util.ContextUtil;
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

    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = authentication.getPrincipal().toString();//讀取輸入的帳號
        String password = authentication.getCredentials().toString();//讀取輸入的密碼
        Employee loginUser =null;//宣告一個變數用來存使用者的資料
        if(!employeeRepository.existsById(account)){ //如果帳號不存在的話
            loginUser =findUserByLDAp(account,password);
        }else{//如果帳號存在
            loginUser =employeeRepository.findById(account).get();//讀取所有該帳號的相關資料
            if(!encoder.matches(password,loginUser.getPassword())){//比對密碼是否相符
                throw new BadCredentialsException("plz check account or password");//如果不相符會丟出一個錯誤訊息
            }
        }
        String[] permissions =getPermissionsList(loginUser);
        EmployeeResponse principle =EmployeeResponse.valueOf(loginUser);
        principle.setPermissions(Arrays.asList(permissions));
        return new UsernamePasswordAuthenticationToken(principle,null, AuthorityUtils.createAuthorityList(permissions));
    }

    private String[] getPermissionsList(Employee loginUser) {
        List<String> permissions =new ArrayList<>();

        permissions.add("ROLE_NORMAL");
        return permissions.toArray(new String[permissions.size()]);
    }

    private Employee findUserByLDAp(String account, String password) {
        LdapUser ldapUser =ldapService.authenticate(account,password);
        if(ldapUser==null){
            throw new BadCredentialsException("plz check account or password");
        }
        return addUserToDataBase(ldapUser,password);
    }

    private Employee addUserToDataBase(LdapUser ldapUser,String password){
        Employee traceUser =new Employee();
        traceUser.setId(ldapUser.getAccount());
        traceUser.setPassword(encoder.encode(password));
        traceUser.setName(ldapUser.getName());
        traceUser.setEmail(ldapUser.getEmail());
        employeeRepository.save(traceUser);
        return traceUser;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass == UsernamePasswordAuthenticationToken.class;
    }
}
