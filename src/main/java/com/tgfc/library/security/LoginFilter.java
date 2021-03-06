package com.tgfc.library.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.library.response.BaseResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    private ObjectMapper mapper = new ObjectMapper();

    public LoginFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        AuthenticationSuccessHandler authenticationSuccessHandler =
                (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) -> {
                    BaseResponse response = new BaseResponse();
                    response.setStatus(true);
                    response.setData(authentication.getPrincipal());
                    httpServletResponse.setHeader("Content-type", "application/json;charset=UTF-8");
                    httpServletResponse.getWriter().println(mapper.writeValueAsString(response));
                };
        AuthenticationFailureHandler authenticationFailureHandler =
                (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) -> {
                    BaseResponse response = new BaseResponse();
                    response.setStatus(false);
                    response.setMessage(e.getMessage());
                    httpServletResponse.setHeader("Content-type", "application/json;charset=UTF-8");
                    httpServletResponse.setStatus(500);
                    httpServletResponse.getWriter().println(mapper.writeValueAsString(response));
                };

        this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        if (httpServletRequest.getMethod().equals("POST") && httpServletRequest.getContentType().startsWith("application/json")) {
            JsonNode root = mapper.readTree(httpServletRequest.getInputStream());
            return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(root.get("account").asText(), root.get("password").asText()));
        } else {
            throw new BadCredentialsException("方法不支援");
        }

    }

}