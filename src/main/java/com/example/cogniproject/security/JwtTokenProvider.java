package com.example.cogniproject.security;

import com.example.cogniproject.exception.InvalidJwtException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Generates and validates JWT tokens using the HS256 algorithm.
 * The secret is provided via the jwt.secret application property (hex-encoded).
 */
@Component
public class JwtTokenProvider {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /** Derives a HMAC-SHA key from the configured base64-encoded secret (jwt.secret property). */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token for the authenticated principal.
     *
     * @param authentication the current Spring Security authentication object
     * @return signed JWT string
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return buildToken(userDetails.getUsername());
    }

    /** Builds a JWT token for the given username. */
    public String generateTokenForUsername(String username) {
        return buildToken(username);
    }

    private String buildToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** Extracts the username (subject) from a valid JWT token. */
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /** Returns the configured token expiration in milliseconds. */
    public long getJwtExpiration() {
        return jwtExpiration;
    }

    /**
     * Validates the given JWT token.
     *
     * @param token the raw JWT string
     * @return true if the token is valid
     * @throws InvalidJwtException if the token is invalid, expired, or malformed
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            log.warn("JWT token is expired");
            throw new InvalidJwtException("JWT token has expired", ex);
        } catch (UnsupportedJwtException ex) {
            log.warn("JWT token is unsupported");
            throw new InvalidJwtException("JWT token is unsupported", ex);
        } catch (MalformedJwtException ex) {
            log.warn("JWT token is malformed");
            throw new InvalidJwtException("JWT token is malformed", ex);
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            log.warn("JWT signature is invalid");
            throw new InvalidJwtException("JWT signature is invalid", ex);
        } catch (IllegalArgumentException ex) {
            log.warn("JWT claims string is empty");
            throw new InvalidJwtException("JWT claims string is empty", ex);
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
