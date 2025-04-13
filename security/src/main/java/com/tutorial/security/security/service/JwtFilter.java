package com.tutorial.security.security.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService; //parses the encrypted Jwt & get the data members
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @Override //method executed everytime we have a request
    protected void doFilterInternal(@NonNull HttpServletRequest request, //can throw servlet exception
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        //check if invoked resource is supposed to have no auth
        if(request.getServletPath().contains("/api/v1/auth")){
            //filterChain.doFilter(request, response) passes the request to the next filter in the chain or the target resource (like a controller),
            // continuing the processing flow.
            filterChain.doFilter(request, response); //can throw TOException
            return; //don't execute next steps
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        final String userEmail;
        if ( authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; //don't execute next steps
        }

        jwt = authHeader.substring(7); //"Bearer " -> 7 chars
        userEmail = jwtService.extractUsername(jwt);

        if(null != userEmail && SecurityContextHolder.getContext().getAuthentication() == null) { //2nd param true mean users is not authenticated & proceed with more checks
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail); //fetching user by email
            if (jwtService.isTokenValid(jwt, userDetails)) { //validate token & user both
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                ); //this object is used/required by spring to update the security context holder

                authenticationToken.setDetails( //extract info from request to build token
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                //updating the security context holder once all tasks done
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); //kind of manually authenticating the user as all checks are done

            }
        }

        //now okay to call the filter chain
        filterChain.doFilter(request, response);
    }

}
