package com.tgfc.library.provider;

import com.tgfc.library.entity.Users;
import com.tgfc.library.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class CustomProvider implements AuthenticationProvider {

    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder(); //密碼加盐

    @Autowired
    IUsersRepository usersRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String account = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        Users users = usersRepository.findByAccount(account);

        if(password == null || !(users.getPassword().equals(password))){
            Logger logger =Logger.getLogger("CustomProvider");
            logger.info(String.format("user %s Not found ", password));
            throw new AuthenticationServiceException(String.format("login fail account = %s ", password));
        }

        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(account,password,grantedAuths);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
