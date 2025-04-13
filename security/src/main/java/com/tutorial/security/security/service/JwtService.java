package com.tutorial.security.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService  {
    //performs all tasks for a JWT token -> will generate token, validates toke, extract after decoding the token

    @Value("${app.security.jwt.expiration:7200}")
    private long jwtExpiration;

    @Value("${app.security.jwt.secret-key}")
    private String secretKey;

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); //2nd param is a function
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); //TODO: check for a better way
    }

    private Date extractExpiration(String token ) {
        return extractClaim(token, Claims::getExpiration); //2nd param is Function, can extract any claim pre-defined in token
    }

    @SuppressWarnings("deprecation")
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser() //type = ParseBuilder
                .setSigningKey(getSignInKey()) //TODO: is deprecated
                .build() //return Jwtparser
                .parseClaimsJws(token) //TODO: is deprecated
                .getBody() ; //TODO: is deprecated
    }

    //takes userdetails of security.core.userdetails
    public String generateJwtToken(UserDetails userDetails) {
        return generateJwtToken(new HashMap<>(), userDetails);
    }

    private String generateJwtToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtExpiration); //custom expiration duration
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long jwtExpiration) {
        var authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts
                .builder()
                .claims(claims) //can be extra claims , setClaims & setSubject deprecated in
                .subject(userDetails.getUsername()) //subject is the username
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) //jwtExpiration type = long & not Long
                .claim("authorities", authorities) //our imp claims
                .signWith(getSignInKey())
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }


//    extractUsername
}
