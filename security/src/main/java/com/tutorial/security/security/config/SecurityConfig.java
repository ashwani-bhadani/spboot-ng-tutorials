package com.tutorial.security.security.config;

import com.tutorial.security.security.service.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
// Enables method-level security in Spring using annotations like @Secured.
//     - @Secured("ROLE_ADMIN") can now be used on controller/service methods.
//     - This must be placed on a @Configuration class (usually your Security config).
//     - Also supports @PreAuthorize / @PostAuthorize if enabled separately.
// if you want to use @PreAuthorize or @PostAuthorize, also enable:
    @EnableMethodSecurity(prePostEnabled = true)
    These annotations let you protect specific methods based on roles or expressions.

For @Bean defined inside a @Configuration class, spring ensures singleton nature of it
But if u declare same bean inside @Component, that bean will be called multiple times/beans if accessed

A class annotated with @Component itself is a singleton by default (unless specified otherwise).
But any @Bean methods inside a @Component class won't behave like true singletons. Each call will return a new instance (unless manually managed).

 */


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    //Marking constructor-injected fields as final is not mandatory but a best practice of clean code
    private final JwtFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean //TODO: handle custom exceptions
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request ->  request.requestMatchers(
                                "/auth/**", //permit login register, account creations
                                          "/v2/api-docs",
                                          "/v3/api-docs",
                                          "/v3/api-docs/**",
                                          "/swagger-resources",
                                          "/swagger-resources/**",
                                          "/configuration/ui",
                                          "/configuration/security",
                                          "/swagger-ui/**",
                                          "/webjars/**",
                                          "/swagger-ui.html"
                        //patterns in URL to look for & not apply authentication TODO: add actuator endpoints too
                        ).permitAll()
                                .anyRequest()
                                .authenticated() // check auth for rest all URLS
                ).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //keep sessions stateless TODO: improve later using dependency added
                ).authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
