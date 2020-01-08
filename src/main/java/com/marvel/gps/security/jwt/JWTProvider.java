package com.marvel.gps.security.jwt;

import com.marvel.gps.security.service.UserPrinciple;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JWTProvider {

    private static final Logger logger = LoggerFactory.getLogger(JWTProvider.class);

    @Value("${gps.app.jwtSecret}")
    private String jwtSecret;

    @Value("${gps.app.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {
        UserPrinciple user = (UserPrinciple) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((user.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken, HttpServletRequest httpServletRequest) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error(String.format("Invalid JWT signature -> Message: %s ", e.getMessage()), e);
            httpServletRequest.setAttribute("authException",e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error(String.format("Invalid JWT token -> Message: %s ", e.getMessage()), e);
            httpServletRequest.setAttribute("authException",e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error(String.format("Expired JWT token -> Message: %s ", e.getMessage()), e);
            httpServletRequest.setAttribute("authException",e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error(String.format("Unsupported JWT token -> Message: %s ", e.getMessage()), e);
            httpServletRequest.setAttribute("authException",e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error(String.format("JWT claims string is empty -> Message: %s ", e.getMessage()), e);
            httpServletRequest.setAttribute("authException",e.getMessage());
        }

        return false;
    }
}
