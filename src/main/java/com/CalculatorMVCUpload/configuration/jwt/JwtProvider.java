package com.CalculatorMVCUpload.configuration.jwt;

import com.CalculatorMVCUpload.entity.RoleEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.util.StringUtils.hasText;

@Component
@Log
public class JwtProvider {

    @Value("$(jwt.access.secret)")
    private String jwtAccessSecret;
    @Value("$(jwt.refresh.secret)")
    private String jwtRefreshSecret;

    public String generateAccessToken(@NonNull String login, RoleEntity roleEntity) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant accessExpirationInstant = now.plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
        final Date accessExpiration = Date.from(accessExpirationInstant);
        return Jwts.builder()
                .setSubject(login)
                .claim("roles", roleEntity.getName())
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS512, jwtAccessSecret)
                .compact();
    }

    public String generateRefreshToken(@NonNull String login) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant refreshExpirationInstant = now/*.plusDays(5)*/.plusMinutes(15).atZone(ZoneId.systemDefault()).toInstant();
        final Date refreshExpiration = Date.from(refreshExpirationInstant);
        return Jwts.builder()
                .setSubject(login)
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS512, jwtRefreshSecret)
                .compact();
    }

    public boolean validateAccessToken(@NonNull String accessToken) {
        return validateToken(accessToken, jwtAccessSecret);
    }

    public boolean validateRefreshToken(@NonNull String refreshToken) {
        return validateToken(refreshToken, jwtRefreshSecret);
    }

    public boolean validateToken(String token, String secretKey) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.severe("invalid token");
        }
        return false;
    }

    public String getLoginFromAccessToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtAccessSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getLoginFromRefreshToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtRefreshSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getRoleFromAccessToken(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtAccessSecret).parseClaimsJws(token).getBody();
        String roles = (String) claims.get("roles");
        return roles;
    }

    public String getTokenFromBearer(String bearer){
        String token = null;
        String userLogin = null;
        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            token = bearer.substring(7);
        }
        if (token != null && validateAccessToken(token)) {
            return token;
        } else return null;
    }
}
