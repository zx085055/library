package com.tgfc.library.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.library.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
    public static class ApiConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        LdapAuthProvider ldapAuthProvider;

        private ObjectMapper mapper =new ObjectMapper();

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.headers().frameOptions().sameOrigin();

            http.cors().and().regexMatcher("/.*")
                    .authorizeRequests()
                    .regexMatchers("/.*")
                    .authenticated()
                    .and()
                    .requestCache()
                    .requestCache(new NullRequestCache())
                    .and()
                    .logout().logoutUrl("/logout").deleteCookies("JSESSIONID")
                    .logoutSuccessHandler((HttpServletRequest var1, HttpServletResponse response, Authentication var3)->{
                        BaseResponse logoutOk =new BaseResponse();
                        logoutOk.setStatus(true);
                        logoutOk.setMessage("logout ok");
                        response.setHeader("Content-type", "application/json;charset=UTF-8");
                        response.getWriter().print(mapper.writeValueAsString(logoutOk));
                    });

            http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);

            http.exceptionHandling()
                    .authenticationEntryPoint((request,response,exception)->{
                        BaseResponse mustLogin = new BaseResponse();
                        mustLogin.setMessage(exception.getMessage());
                        response.setStatus(401);
                        response.setHeader("Content-type", "application/json;charset=UTF-8");
                        response.getWriter().print(mapper.writeValueAsString(mustLogin));
                    })
                    .accessDeniedHandler((request, response,accessDeniedException) -> {
                        BaseResponse permission_denied = new BaseResponse();
                        permission_denied.setMessage(accessDeniedException.getMessage());
                        response.setStatus(403);
                        response.setHeader("Content-type", "application/json;charset=UTF-8");
                        response.getWriter().print(mapper.writeValueAsString(permission_denied));
                    });
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {

            auth.authenticationProvider(ldapAuthProvider);
        }

        @Bean
        public LoginFilter loginFilter() throws Exception {
            LoginFilter filter = new LoginFilter("/login");
            filter.setAuthenticationManager(authenticationManager());
            return filter;
        }
    }



}
