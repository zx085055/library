package com.tgfc.library.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.library.advice.CommonResponse;
import com.tgfc.library.enums.AuthenticationErrorCode;
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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.savedrequest.NullRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class LibrarySecurityConfig {

    @Configuration
    @Order(1)
    @EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
    public static class ApiConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        LdapAuthProvider ldapAuthProvider;

        private ObjectMapper mapper =new ObjectMapper();

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.headers().frameOptions().sameOrigin();
            http.cors().and().antMatcher("/**")
                    .authorizeRequests()
                    .antMatchers("/*")
                    .authenticated()
                    .and()
                    .requestCache()
                    .requestCache(new NullRequestCache())
                    .and()
                    .logout().logoutUrl("/logout").deleteCookies("JSESSIONID","XSRF-TOKEN")
                    .logoutSuccessHandler((HttpServletRequest var1, HttpServletResponse response, Authentication var3)->{
                        CommonResponse logoutOk =new CommonResponse(true,null,"logout ok");
                        response.setHeader("Content-type", "application/json;charset=UTF-8");
                        response.getWriter().print(mapper.writeValueAsString(logoutOk));
                    });
            http.csrf().ignoringAntMatchers("/login*");
            http.csrf().ignoringAntMatchers("/logout*");
            http.csrf().csrfTokenRepository(new CookieCsrfTokenRepository());

            http.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class);

            http.exceptionHandling()
                    .authenticationEntryPoint((request,response,exception)->{
                        CommonResponse mustLogin = new CommonResponse( AuthenticationErrorCode.MUST_LOGIN, exception.getMessage());
                        response.setStatus(401);
                        response.setHeader("Content-type", "application/json;charset=UTF-8");
                        response.getWriter().print(mapper.writeValueAsString(mustLogin));
                    })
                    .accessDeniedHandler((request, response,accessDeniedException) -> {
                        CommonResponse permission_denied = new CommonResponse( AuthenticationErrorCode.PERMISSION_DENIED, accessDeniedException.getMessage());
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

    @Configuration
    @Order(2)
    public static class WebSocketConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http.cors().and().csrf().disable().antMatcher("/library-websocket*").authorizeRequests().anyRequest().permitAll();
        }
    }

}