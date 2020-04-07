package com.meicorl.shopping_mall_miniapp.configurations;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser("meicorl")
                .password("$2a$10$zSt3bx8MseiR.OADxLjtx.4IUW8eJNC9Wd3Rz628QLuhI741N3ITy")
                .roles("USER", "ADMIN")
                .and()
                .withUser("myuser")
                .password("$2a$10$SF73KdiettPVqs5JX8VZue0ZvzCgOomz5GqhpH5zs4fzfahmDLva6")
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] endPoints = {"auditevents", "beans", "conditions", "configroups", "env", "flyway", "httptrace", "loggers", "liquibase", "metrics", "mappings", "scheduledtasks", "sessions", "shutdown", "threaddump"};
        http.requestMatcher(EndpointRequest.to(endPoints))
                .authorizeRequests().anyRequest()
                .hasRole("ADMIN")
                .and()
                .antMatcher("/close").authorizeRequests().anyRequest().hasRole("ADMIN")
                .and()
                .httpBasic();
    }
}
