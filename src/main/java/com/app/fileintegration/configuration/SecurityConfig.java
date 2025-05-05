package com.app.fileintegration.configuration;

import com.app.fileintegration.filter.JwtFilter;
import com.app.fileintegration.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/api/auth/register","/api/auth/login").permitAll()
                            .requestMatchers("/swagger-ui/**", "/swagger-ui.html/**", "/v3/api-docs/**").permitAll()
                            .anyRequest().authenticated();
                        })
                .addFilterBefore(new JwtFilter(userDetailsService, jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(s ->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(withDefaults())
                .formLogin(withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,PasswordEncoder passwordEncoder) throws  Exception{
        AuthenticationManagerBuilder authenticationManagerBuilder =http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return  authenticationManagerBuilder.build();
    }
}
