package com.tgfc.library.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.library.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @EnableGlobalMethodSecurity(jsr250Enabled = true)
    public static class ApiConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        LdapAuthProvider ldapAuthProvider;

        private ObjectMapper mapper = new ObjectMapper();

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
                    .logout().logoutUrl("/logout").deleteCookies("JSESSIONID").logoutSuccessUrl("/logout").logoutSuccessHandler(logoutFilter());

            http.exceptionHandling()
                    .authenticationEntryPoint((request, response, exception) -> {
                        BaseResponse mustLogin = new BaseResponse();
                        mustLogin.setMessage("請登入後再使用");
                        response.setStatus(401);
                        response.setHeader("Content-type", "application/json;charset=UTF-8");
                        response.getWriter().print(mapper.writeValueAsString(mustLogin));
                    });

            http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);


        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(ldapAuthProvider);
        }

        @Bean
        public LoginFilter loginFilter() throws Exception {
            LoginFilter filter = new LoginFilter("/login");
            filter.setAuthenticationManager(authenticationManager());
            return filter;
        }

        @Bean
        public LogoutSuccessHandler logoutFilter() {
            return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
                BaseResponse logoutOk = new BaseResponse();
                logoutOk.setStatus(true);
                logoutOk.setMessage("登出成功");
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                response.getWriter().print(mapper.writeValueAsString(logoutOk));
            };
        }
    }


}
