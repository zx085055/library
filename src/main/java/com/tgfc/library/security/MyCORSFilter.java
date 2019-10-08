package com.tgfc.library.security;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyCORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        httpResponse.setHeader("Access-Control-Allow-Origin","*");
        httpResponse.setHeader("Access-Control-Allow-Methods","POST,GET,PUT,OPTIONS,DELETE");
        httpResponse.setHeader("Access-Control-Allow-Headers","Content-Type,Authorization");
        httpResponse.setHeader("Access-Control-Max-Age","4800");

        if (httpRequest.getMethod()!=null&&httpRequest.getMethod().equals("OPTIONS"))
            return;

        filterChain.doFilter(servletRequest,servletResponse);

    }
}
