package com.jojoldu.book.springboot.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/","/login", "/signUp", "/categorySelect", "/css/**", "/fonts/**", "/images/**", "/js/**", "/vendor/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
        .loginProcessingUrl("/doLogin")
        .usernameParameter("id")
        .passwordParameter("pw")
        .successHandler(new MyLoginSuccessHandler())
        .and()
        .logout()
        .logoutUrl("/doLogout")
        .logoutSuccessUrl("/login");
    }
    @Bean
    PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }


}
