package com.meicorl.shopping_mall_miniapp.configurations;

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
                .password("$2a$10$SgEy7ClVQmfbIgNbI6M3LON3dxMrS6cUBWLhlBVxrtkcDrbQem8.6")
                .roles("USER", "ADMIN")
                .and()
                .withUser("myuser")
                .password("$2a$10$SF73KdiettPVqs5JX8VZue0ZvzCgOomz5GqhpH5zs4fzfahmDLva6")
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        String[] endPoints = {"auditevents", "beans", "conditions", "configroups", "env", "flyway", "httptrace", "loggers", "liquibase", "metrics", "mappings", "scheduledtasks", "sessions", "shutdown", "threaddump"};
        http.authorizeRequests()
                // 任何请求都需要通过登录验证
//                .anyRequest().authenticated()
                .antMatchers("/actuator/**")
                .hasRole("ADMIN")
                .and()
                .antMatcher("/close").authorizeRequests().anyRequest().hasRole("ADMIN")
                .and()
                .httpBasic();
    }
}
