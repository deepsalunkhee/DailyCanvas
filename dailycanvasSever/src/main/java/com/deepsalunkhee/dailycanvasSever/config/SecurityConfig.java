package com.deepsalunkhee.dailycanvasSever.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

     private final JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

    public SecurityConfig(JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler) {
        this.jwtAuthenticationSuccessHandler = jwtAuthenticationSuccessHandler;
    }

   
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/","/error","/oauth/**").permitAll()
                .anyRequest().authenticated()          
                );
        http.oauth2Login(Customizer.withDefaults());

        //on success redirect to angular app

        http.oauth2Login().successHandler(jwtAuthenticationSuccessHandler);

        

        return http.build();
    }
}
