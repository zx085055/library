package com.tgfc.library.conf;

import com.tgfc.library.provider.CustomProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@ComponentScan("com.tgfc.library")
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    CustomProvider customProvider;

    /**
     * 初始化 security 配置檔案
     * @param http security 配置檔案
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //關閉 csrf 防護
        http.csrf().disable();
        http.authorizeRequests().anyRequest().authenticated();//設定權限
        http.formLogin().loginProcessingUrl("/api/login");    //設定登入

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(customProvider);  //設定驗證器
    }




}