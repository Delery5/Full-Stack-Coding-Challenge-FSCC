package com.ibm.delery.loginservice.security;

import com.ibm.delery.loginservice.exception.JwtAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;


    // generate JWT token
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
        return token;
    }

    private Key key() {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret));
    }

    // get username fro Jwt token
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        String username = claims.getSubject();
        return username;
    }


    // validate Jwt token
    public Boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new JwtAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException e) {
            // Attempt to refresh the expired token
            String refreshedToken = refreshExpiredToken(token);

            // Validate the refreshed token
            try {
                Jwts.parserBuilder()
                        .setSigningKey(key())
                        .build()
                        .parse(refreshedToken);
                return true;
            } catch (Exception ex) {
                throw new JwtAPIException(HttpStatus.BAD_REQUEST, "Refreshed JWT token is also invalid");
            }
        } catch (UnsupportedJwtException e) {
            throw new JwtAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            throw new JwtAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        }
    }
    private String refreshExpiredToken(String expiredToken) {
        // Placeholder logic to refresh the token
        // This might involve making an HTTP request to the authentication server's token refresh endpoint

        String refreshTokenEndpoint = "http://localhost:8083/api/auth/validate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Include expired token in the request body or headers as needed by your authentication server
        // In a real scenario, you might need to include additional parameters like client ID, client secret, etc.
        // Here, we assume the expired token is included in the request body as a JSON field "token"
        String requestJson = "{\"token\": \"" + expiredToken + "\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> responseEntity = new RestTemplate().postForEntity(refreshTokenEndpoint, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Extract the refreshed token from the response body
            String refreshedToken = responseEntity.getBody();
            return refreshedToken;
        } else {
            throw new JwtAPIException(HttpStatus.BAD_REQUEST, "Token refresh failed");
        }
    }
    }
